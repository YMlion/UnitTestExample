package cn.futu.loginunittest;

import java.util.List;

import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.User;

/**
 * View、Presenter
 */
class MainContract
{
    interface View
    {

        void onLoginStart();

        void onLoginSuccess(User user);

        void verifyPhoneCode(User user);

        void onLoginFailed(String tips);

        void changeInputState(boolean phoneError, boolean pwdError);

        void onLoadStart();

        void showContactList(List<Contact> contacts);

        void showEmpty();

        void onLoadFailed(String message);
    }

    interface Presenter
    {

        void login(String phoneNum, String password);

        void check(String phoneNum, String password);

        void onDestroy();

        void loadContactList();
    }
}
