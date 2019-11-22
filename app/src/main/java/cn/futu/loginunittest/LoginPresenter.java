package cn.futu.loginunittest;

import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.Result;
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
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public LoginPresenter(MainContract.View view, Repository repository)
    {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void login(String phoneNum, String password)
    {
        // 登录时回调，用于显示loading
        mView.onLoginStart();
        // 请求数据
        mDisposable.add(mRepository.login(phoneNum, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result<User>>()
                {
                    @Override
                    public void accept(Result<User> result)
                    {
                        // 请求成功
                        if (result instanceof Result.Error) // 登录失败
                        {
                            mView.onLoginFailed(((Result.Error) result).getError().getMessage());
                        }
                        else if (result instanceof Result.Success) // 登录成功
                        {
                            User user = (User) ((Result.Success) result).getData();
                            if (user.isVerifyPhoneCode()) // 需要验证码
                            {
                                mView.verifyPhoneCode(user);
                            }
                            else
                            { // 跳转到通讯录列表
                                mView.onLoginSuccess(user);
                            }
                        }
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) // 请求失败，出现某些异常，如网络异常
                    {
                        mView.onLoginFailed(throwable.getMessage());
                    }
                })
        );
    }

    /**
     * 手机号与密码格式验证
     */
    @Override
    public void check(String phoneNum, String password)
    {
        boolean phoneError = true, pwdError = true;
        // 11位，1开头
        if (phoneNum != null && phoneNum.length() == 11 && phoneNum.startsWith("1"))
        {
            phoneError = false;
        }
        // 密码6-18位
        if (password != null && password.length() >= 6 && password.length() <= 18)
        {
            pwdError = false;
        }
        // 修改帐号密码及登录按钮状态
        mView.changeInputState(phoneError, pwdError);
    }

    @Override
    public void destroy()
    {
        mView = null;
        mDisposable.clear();
    }
}
