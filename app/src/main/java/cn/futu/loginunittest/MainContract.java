package cn.futu.loginunittest;

import cn.futu.loginunittest.data.model.User;

/**
 * View、Presenter
 */
class MainContract
{
    interface View
    {
        /**
         * 加载时回调
         */
        void onLoginStart();

        /**
         * 登录成功
         */
        void onLoginSuccess(User user);

        /**
         * 提示验证手机
         */
        void verifyPhoneCode(User user);

        /**
         * 登录失败
         */
        void onLoginFailed(String tips);

        /**
         * 手机、密码输入状态设置
         */
        void changeInputState(boolean phoneError, boolean pwdError);

    }

    interface Presenter
    {

        /**
         * 登录
         */
        void login(String phoneNum, String password);

        /**
         * 检查输入的手机和密码
         */
        void check(String phoneNum, String password);

        /**
         * 销毁回调
         */
        void onDestroy();

    }
}
