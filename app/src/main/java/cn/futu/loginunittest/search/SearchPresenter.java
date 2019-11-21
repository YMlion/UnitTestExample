package cn.futu.loginunittest.search;

import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.model.PhoneInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * search presenter
 */
public class SearchPresenter implements SearchContract.Presenter
{

    private SearchContract.View mView;
    private Repository mRepository;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public SearchPresenter(SearchContract.View view, Repository repository)
    {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void onDestroy()
    {
        mView = null;
        mDisposable.clear();
    }

    @Override
    public void search(String phone)
    {
        if (!checkPhone(phone))
        {
            mView.onSearchFailed("请输入正确格式的电话号码");
            return;
        }
        mView.onSearchStart();

        mDisposable.add(mRepository.searchPhone(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PhoneInfo>()
                {
                    @Override
                    public void accept(PhoneInfo phoneInfo)
                    {
                        if (phoneInfo.isPublic())
                        {
                            mView.showPublicInfo(phoneInfo);
                        }
                        else
                        {
                            mView.showPersonalInfo(phoneInfo);
                        }
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable)
                    {
                        if (throwable instanceof PhoneInfo.NotFoundException)
                        {
                            mView.showNoResult();
                        }
                        else
                        {
                            mView.onSearchFailed(throwable.getMessage());
                        }
                    }
                }));
    }

    private boolean checkPhone(String phone)
    {
        return phone != null && phone.length() >= 3 && phone.length() <= 11;
    }
}
