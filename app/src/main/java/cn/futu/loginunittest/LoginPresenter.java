package cn.futu.loginunittest;

import cn.futu.loginunittest.data.LoginRepository;
import cn.futu.loginunittest.data.Result;
import cn.futu.loginunittest.data.model.LoggedInUser;

/**
 * presenter
 */
public class LoginPresenter implements MainContract.Presenter
{

    private MainContract.View mView;
    private LoginRepository mRepository;

    public LoginPresenter(MainContract.View view, LoginRepository repository)
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
            LoggedInUser user = (LoggedInUser) ((Result.Success) result).getData();
            if (user.isVerifyPhoneCode())
            {
                mView.verifyPhoneCode(user);
            }
            else
            {
                mView.onLoginSuccess(user);
            }
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
    public void onDestroy()
    {
        mView = null;
    }
}