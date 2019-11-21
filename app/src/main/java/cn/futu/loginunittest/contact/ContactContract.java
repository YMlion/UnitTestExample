package cn.futu.loginunittest.contact;

import java.util.List;

import cn.futu.loginunittest.data.model.Contact;

/**
 * view and presenter interface
 */
public class ContactContract
{
    interface View
    {

        /**
         * 加载列表时回调
         */
        void onLoadStart();

        /**
         * 显示联系人列表
         */
        void showContactList(List<Contact> contacts);

        /**
         * 显示空状态
         */
        void showEmpty();

        /**
         * 加载失败
         */
        void onLoadFailed(String message);
    }

    interface Presenter
    {

        /**
         * 销毁
         */
        void onDestroy();

        /**
         * 加载联系人列表
         */
        void loadContactList();
    }
}
