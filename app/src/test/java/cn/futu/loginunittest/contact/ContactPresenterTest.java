package cn.futu.loginunittest.contact;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import cn.futu.loginunittest.RxRule;
import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.User;
import io.reactivex.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * contact presenter unit test
 */
public class ContactPresenterTest
{

    @Rule
    public RxRule mRxRule = new RxRule();

    private Repository mRepository;

    private ContactContract.View mView;

    private ContactPresenter mPresenter;

    private static final String ID = "000001";

    @Before
    public void setUp()
    {
        mRepository = mock(Repository.class);
        mView = mock(ContactContract.View.class);
        mPresenter = new ContactPresenter(mView, mRepository, ID);
    }


    @Test(expected = NullPointerException.class)
    public void onDestroy()
    {
        mPresenter.destroy();
        mPresenter.loadContactList();
    }

    @Test
    public void loadContactList_cacheAndNetNormal()
    {
        // 数据准备
        final User user = new User(ID, "", false);
        List<Contact> cacheList = new ArrayList<>();
        cacheList.add(new Contact(1, "", ""));
        List<Contact> netList = new ArrayList<>();
        netList.add(new Contact(1, "", ""));
        // mock
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.just(cacheList),
                Observable.just(netList)));
        // 执行
        mPresenter.loadContactList();
        // 验证结果
        verify(mView, times(2)).showContactList((List<Contact>) any());// 执行了2次
        InOrder order = inOrder(mView, mRepository);
        order.verify(mView).onLoadStart();
        order.verify(mRepository).loadContactList(user.getUserId());
        order.verify(mView).showContactList(cacheList);
        order.verify(mView).showContactList(netList);
    }

    @Test
    public void loadContactList_cacheAndNetEmpty()
    {
        // 数据准备
        final User user = new User(ID, "", false);
        List<Contact> cacheList = new ArrayList<>();
        cacheList.add(new Contact(1, "", ""));
        List<Contact> netEmptyList = new ArrayList<>();
        // mock
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.just(cacheList),
                Observable.just(netEmptyList)));
        // 执行
        mPresenter.loadContactList();
        // 验证结果
        verify(mView).onLoadStart();
        verify(mRepository).loadContactList(user.getUserId());
        verify(mView).showContactList(cacheList);
        verify(mView).showEmpty();
    }

    @Test
    public void loadContactList_cacheAndNetError()
    {
        // 数据准备
        final User user = new User(ID, "", false);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "", ""));

        final String error = "error";
        // mock
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.just(contacts),
                Observable.<List<Contact>>error(new Exception(error))));
        // 执行
        mPresenter.loadContactList();
        // 验证结果
        verify(mView).onLoadStart();
        verify(mRepository).loadContactList(user.getUserId());
        verify(mView).showContactList(contacts);
        verify(mView).onLoadFailed(error);
    }

    @Test
    public void loadContactList_noCacheAndNetNormal()
    {
        // 数据准备
        final User user = new User(ID, "", false);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "", ""));
        // mock
        when(mRepository.loadContactList(user.getUserId()))
                .thenReturn(Observable.concat(Observable.<List<Contact>>empty(), Observable.just(contacts)));
        // 执行
        mPresenter.loadContactList();
        // 验证结果
        verify(mView).onLoadStart();
        verify(mRepository).loadContactList(user.getUserId());
        verify(mView).showContactList(contacts);
    }

    @Test
    public void loadContactList_noCacheAndNetEmpty()
    {
        // 数据准备
        final User user = new User(ID, "", false);
        List<Contact> contacts = new ArrayList<>();
        // mock
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.<List<Contact>>empty(),
                Observable.just(contacts)));
        // 执行
        mPresenter.loadContactList();
        // 验证结果
        verify(mView).onLoadStart();
        verify(mRepository).loadContactList(user.getUserId());
        verify(mView).showEmpty();
        verify(mView, never()).showContactList(anyList());
    }

    @Test
    public void loadContactList_noCacheAndNetError()
    {
        // 数据准备
        final User user = new User(ID, "", false);
        final String error = "error";
        // mock
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.<List<Contact>>empty(),
                Observable.<List<Contact>>error(new Exception(error))));
        // 执行
        mPresenter.loadContactList();
        // 验证结果
        verify(mView).onLoadStart();
        verify(mRepository).loadContactList(user.getUserId());
        verify(mView).onLoadFailed(error);
        verify(mView, never()).showContactList(anyList());
    }
}