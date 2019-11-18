package cn.futu.loginunittest.data;

import java.util.List;
import java.util.concurrent.Callable;

import cn.futu.loginunittest.data.model.Contact;
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

    public Result<User> login(String username, String password)
    {
        // handle login
        return remoteDataSource.login(username, password);
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
}
