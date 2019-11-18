package cn.futu.loginunittest;

import java.util.List;

import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.Result;
import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * presenter
 */
public class LoginPresenter implements MainContract.Presenter
{

    private MainContract.View mView;
    private Repository mRepository;
    private User mUser;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public LoginPresenter(MainContract.View view, Repository repository)
    {
        mView = view;
        mRepository = repository;

    }

    @Override
    public void login(String phoneNum, String password)
    {
        mView.onLoginStart();
        Result result = mRepository.login(phoneNum, password);
        if (result instanceof Result.Error)
        {
            mView.onLoginFailed(((Result.Error) result).getError().getMessage());
        }
        else if (result instanceof Result.Success)
        {
            User user = (User) ((Result.Success) result).getData();
            if (user.isVerifyPhoneCode())
            {
                mView.verifyPhoneCode(user);
            }
            else
            {
                mView.onLoginSuccess(user);
            }
            mUser = user;
        }
    }

    /**
     * 手机号与密码格式验证
     */
    @Override
    public void check(String phoneNum, String password)
    {
        boolean phoneError = true, pwdError = true;
        if (phoneNum != null && phoneNum.length() == 11 && phoneNum.startsWith("1"))
        {
            phoneError = false;
        }
        if (password != null && password.length() >= 6 && password.length() <= 18)
        {
            pwdError = false;
        }
        mView.changeInputState(phoneError, pwdError);
    }

    @Override
    public void loadContactList()
    {
        mView.onLoadStart();
        mDisposable.add(mRepository.loadContactList(mUser.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Contact>>()
                {
                    @Override
                    public void accept(List<Contact> contacts)
                    {
                        if (contacts != null && !contacts.isEmpty())
                        {
                            mView.showContactList(contacts);
                        }
                        else
                        {
                            mView.showEmpty();
                        }
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable)
                    {
                        mView.onLoadFailed(throwable.getMessage());
                    }
                }));
    }

    @Override
    public void onDestroy()
    {
        mView = null;
        mDisposable.clear();
    }
}
