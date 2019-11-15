package cn.futu.loginunittest;

import cn.futu.loginunittest.data.model.LoggedInUser;

/**
 * View、Presenter
 */
class MainContract
{
    interface View
    {

        void onLoginStart();

        void onLoginSuccess(LoggedInUser user);

        void verifyPhoneCode(LoggedInUser user);

        void onLoginFailed(String tips);

        void changeInputState(boolean phoneError, boolean pwdError);
    }

    interface Presenter
    {

        void login(String phoneNum, String password);

        void check(String phoneNum, String password);

        void onDestroy();

    }
}
