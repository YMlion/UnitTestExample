package cn.futu.loginunittest.search;

import cn.futu.loginunittest.data.model.PhoneInfo;

/**
 * view and presenter interface
 */
public class SearchContract
{
    interface View
    {

        /**
         * 搜索开始时回调
         */
        void onSearchStart();

        /**
         * 显示公共电话信息
         */
        void showPublicInfo(PhoneInfo phoneInfo);

        /**
         * 显示个人电话信息
         */
        void showPersonalInfo(PhoneInfo phoneInfo);

        /**
         * 未找到电话信息
         */
        void showNoResult();

        /**
         * 搜索失败
         */
        void onSearchFailed(String message);
    }

    interface Presenter
    {

        /**
         * 销毁
         */
        void onDestroy();

        /**
         * 搜索电话号码
         */
        void search(String phone);
    }
}
