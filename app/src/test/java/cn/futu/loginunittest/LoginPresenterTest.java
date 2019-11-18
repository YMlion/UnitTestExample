package cn.futu.loginunittest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.Result;
import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.User;
import io.reactivex.Observable;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 测试用例
 */
@RunWith(JUnitParamsRunner.class)
public class LoginPresenterTest
{

    @Rule
    public RxRule mRxRule = new RxRule();

    @Mock
    private Repository mRepository;

    @Mock
    private MainContract.View mView;

    private LoginPresenter mPresenter;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        mPresenter = new LoginPresenter(mView, mRepository);
    }

    @Test
    public void login_success()
    {
        final User user = new User("000001", "小明", false);
        when(mRepository.login(anyString(), anyString())).thenReturn(new Result.Success(user));

        mPresenter.login("12345678901", "123456");
        verify(mView).onLoginStart();
        verify(mView).onLoginSuccess(user);
    }

    @Test
    public void login_verifyCode()
    {
        final User user = new User("000001", "小明", true);
        when(mRepository.login(anyString(), anyString())).thenReturn(new Result.Success(user));

        mPresenter.login("12345678901", "123456");
        verify(mView).onLoginStart();
        verify(mView).verifyPhoneCode(user);
    }

    @Test
    public void login_failed()
    {
        final String tips = "网络错误";
        when(mRepository.login(anyString(), anyString())).thenReturn(new Result.Error(new Exception(tips)));

        mPresenter.login("12345678901", "123456");
        verify(mView).onLoginStart();
        verify(mView).onLoginFailed(tips);
    }

    @Test
    @Parameters(method = "collections")
    public void check(String phone, String password, boolean phoneExcepted,
            boolean passwordExcepted)
    {
        mPresenter.check(phone, password);
        verify(mView).changeInputState(phoneExcepted, passwordExcepted);
    }

    /**
     * 获取测试数据
     */
    private Collection<Object[]> collections()
    {
        return Arrays.asList(new Object[][]{
                {"12345678901", "123456", false, false},
                {"12345678901", "123456789012345678", false, false},
                {"12345678901", "12345", false, true},
                {"12345678901", "1234567890123456789", false, true},
                {"1234567890", "123456", true, false},
                {"123456789012", "123456", true, false},
                {"22345678901", "123456", true, false},
                {null, null, true, true},
                {"", "", true, true}
        });
    }

    @Test(expected = NullPointerException.class)
    public void onDestroy()
    {
        mPresenter.onDestroy();
        mPresenter.login("", "");
    }

    @Test
    public void loadContactList_cacheAndNet()
    {
        final User user = new User("007", "", false);
        Whitebox.setInternalState(mPresenter, "mUser", user);
        List<Contact> contacts1 = new ArrayList<>();
        contacts1.add(new Contact(1, "", ""));
        List<Contact> contacts2 = new ArrayList<>();
        contacts2.add(new Contact(1, "", ""));

        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.just(contacts1),
                Observable.just(contacts2)));

        mPresenter.loadContactList();

        verify(mView).onLoadStart();
        verify(mView, times(2)).showContactList((List<Contact>) any());
        InOrder order = inOrder(mView);
        order.verify(mView).showContactList(contacts1);
        order.verify(mView).showContactList(contacts2);
    }

    @Test
    public void loadContactList_justCache()
    {
        final User user = new User("007", "", false);
        Whitebox.setInternalState(mPresenter, "mUser", user);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "", ""));

        final String error = "error";
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.just(contacts),
                Observable.<List<Contact>>error(new Exception(error))));

        mPresenter.loadContactList();

        verify(mView).onLoadStart();
        verify(mView).showContactList(contacts);
        verify(mView).onLoadFailed(error);
    }

    @Test
    public void loadContactList_justNet()
    {
        final User user = new User("007", "", false);
        Whitebox.setInternalState(mPresenter, "mUser", user);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "", ""));

        when(mRepository.loadContactList(user.getUserId()))
                .thenReturn(Observable.concat(Observable.<List<Contact>>empty(), Observable.just(contacts)));

        mPresenter.loadContactList();

        verify(mView).onLoadStart();
        verify(mView).showContactList(contacts);
    }

    @Test
    public void loadContactList_error()
    {
        final User user = new User("007", "", false);
        Whitebox.setInternalState(mPresenter, "mUser", user);

        final String error = "error";
        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.<List<Contact>>empty(),
                Observable.<List<Contact>>error(new Exception(error))));

        mPresenter.loadContactList();

        verify(mView).onLoadStart();
        verify(mView).onLoadFailed(error);
    }

    @Test
    public void loadContactList_empty()
    {
        final User user = new User("007", "", false);
        Whitebox.setInternalState(mPresenter, "mUser", user);
        List<Contact> contacts1 = new ArrayList<>();
        contacts1.add(new Contact(1, "", ""));
        List<Contact> contacts2 = new ArrayList<>();

        when(mRepository.loadContactList(user.getUserId())).thenReturn(Observable.concat(Observable.just(contacts1),
                Observable.just(contacts2)));

        mPresenter.loadContactList();

        verify(mView).onLoadStart();
        verify(mView).showContactList(contacts1);
        verify(mView).showEmpty();
    }

}