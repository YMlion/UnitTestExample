package cn.futu.loginunittest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import cn.futu.loginunittest.data.LoginRepository;
import cn.futu.loginunittest.data.Result;
import cn.futu.loginunittest.data.model.LoggedInUser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 测试用例
 */
@RunWith(JUnitParamsRunner.class)
public class LoginPresenterTest
{

    @Mock
    private LoginRepository mRepository;

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
        final LoggedInUser user = new LoggedInUser("000001", "小明", false);
        when(mRepository.login(anyString(), anyString())).thenReturn(new Result.Success(user));

        mPresenter.login("12345678901", "123456");
        verify(mView).onLoginStart();
        verify(mView).onLoginSuccess(user);
    }

    @Test
    public void login_verifyCode()
    {
        final LoggedInUser user = new LoggedInUser("000001", "小明", true);
        when(mRepository.login(anyString(), anyString())).thenReturn(new Result.Success(user));

        mPresenter.login("12345678901", "123456");
        verify(mView).onLoginStart();
        verify(mView).verifyPhoneCode(user);
    }

    @Test
    public void login_failed()
    {
        final String tips = "网络错误";
        when(mRepository.login(anyString(), anyString())).thenReturn(
                new Result.Error(new Exception(tips)));

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
}