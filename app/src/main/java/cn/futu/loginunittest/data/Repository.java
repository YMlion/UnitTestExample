package cn.futu.loginunittest.data;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.PhoneInfo;
import cn.futu.loginunittest.data.model.User;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * login data repository
 */
public class Repository
{

    private static volatile Repository instance;

    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;

    // private constructor : singleton access
    private Repository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource)
    {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static Repository getInstance(RemoteDataSource remoteDataSource, LocalDataSource localDataSource)
    {
        if (instance == null)
        {
            instance = new Repository(remoteDataSource, localDataSource);
        }
        return instance;
    }

    public Observable<Result<User>> login(final String phone, final String password)
    {
        return Observable.timer(1, TimeUnit.SECONDS)
                .map(new Function<Long, Result<User>>()
                {
                    @Override
                    public Result<User> apply(Long aLong)
                    {
                        return remoteDataSource.login(phone, password);
                    }
                });
    }

    public Observable<List<Contact>> loadContactList(final String id)
    {
        Observable<List<Contact>> cache = Observable.fromCallable(new Callable<Result<List<Contact>>>()
        {
            @Override
            public Result<List<Contact>> call()
            {
                return localDataSource.loadContact(id);
            }
        }).filter(new Predicate<Result<List<Contact>>>()
        {
            @Override
            public boolean test(Result<List<Contact>> listResult)
            {
                return listResult instanceof Result.Success && ((Result.Success) listResult).getData() != null;
            }
        }).map(new Function<Result<List<Contact>>, List<Contact>>()
        {
            @Override
            public List<Contact> apply(Result<List<Contact>> listResult)
            {
                return (List<Contact>) ((Result.Success) listResult).getData();
            }
        });

        Observable<List<Contact>> net = Observable.fromCallable(new Callable<Result<List<Contact>>>()
        {
            @Override
            public Result<List<Contact>> call()
            {
                return remoteDataSource.loadContact(id);
            }
        }).flatMap(new Function<Result<List<Contact>>, ObservableSource<List<Contact>>>()
        {
            @Override
            public ObservableSource<List<Contact>> apply(Result<List<Contact>> listResult)
            {
                if (listResult instanceof Result.Success)
                {
                    return Observable.just((List<Contact>) ((Result.Success) listResult).getData());
                }
                else
                {
                    return Observable.error(new Exception("加载失败"));
                }
            }
        });

        return Observable.concat(cache, net);

    }

    public Observable<PhoneInfo> searchPhone(String phone)
    {
        return Observable.just(phone)
                .flatMap(new Function<String, ObservableSource<PhoneInfo>>()
                {
                    @Override
                    public ObservableSource<PhoneInfo> apply(String s)
                    {
                        final Result result = remoteDataSource.searchPhone(s);
                        if (result instanceof Result.Success)
                        {
                            final Object data = ((Result.Success) result).getData();
                            if (data instanceof PhoneInfo)
                            {
                                return Observable.just((PhoneInfo) data);
                            }
                            else
                            {
                                return Observable.error(new PhoneInfo.NotFoundException(data.toString()));
                            }
                        }
                        return Observable.error(((Result.Error) result).getError());
                    }
                });
    }
}
