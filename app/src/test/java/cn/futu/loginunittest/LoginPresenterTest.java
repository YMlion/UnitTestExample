package cn.futu.loginunittest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import cn.futu.loginunittest.data.Repository;
import cn.futu.loginunittest.data.Result;
import cn.futu.loginunittest.data.model.User;
import io.reactivex.Observable;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
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
        // 数据准备
        final User user = new User("000001", "小明", false);
        // mock
        when(mRepository.login(anyString(), anyString())).thenReturn(Observable.<Result<User>>just(new Result.Success(user)));
        // 执行
        mPresenter.login("12345678901", "123456");
        // 验证结果
        InOrder inOrder = inOrder(mView, mRepository);
        inOrder.verify(mView).onLoginStart();
        inOrder.verify(mRepository).login(anyString(), anyString()); // 可以匹配，可以写实际参数，匹配则都是匹配
        inOrder.verify(mView).onLoginSuccess(user);
    }

    @Test
    public void login_verifyCode()
    {
        // 数据准备
        final User user = new User("000001", "小明", true);
        // mock
        when(mRepository.login(anyString(), anyString())).thenReturn(Observable.<Result<User>>just(new Result.Success(user)));
        // 执行
        mPresenter.login("12345678901", "123456");
        // 验证结果
        verify(mRepository).login(anyString(), anyString()); // 没有顺序
        verify(mView).onLoginStart();
        verify(mView).verifyPhoneCode(user);
    }

    @Test
    public void login_failed()
    {
        // 数据准备
        final String tips = "密码错误";
        // mock
        when(mRepository.login(anyString(), anyString())).thenReturn(Observable.<Result<User>>just(new Result.Error(new Exception(tips))));
        // 执行
        mPresenter.login("12345678901", "123456");
        // 验证结果
        verify(mView).onLoginStart();
        verify(mRepository).login(anyString(), anyString());
        verify(mView).onLoginFailed(tips);
    }

    @Test
    public void login_error()
    {
        // 数据准备
        final String tips = "网络错误";
        // mock
        when(mRepository.login(anyString(), anyString())).thenReturn(Observable.<Result<User>>error(new Exception(tips)));
        // 执行
        mPresenter.login("12345678901", "123456");
        // 验证结果
        InOrder inOrder = inOrder(mView, mRepository);
        inOrder.verify(mView).onLoginStart();
        // 捕获参数
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        inOrder.verify(mRepository).login(captor.capture(), captor.capture());
        // 验证参数
        assertEquals("12345678901", captor.getAllValues().get(0));
        assertEquals("123456", captor.getAllValues().get(1));

        inOrder.verify(mView).onLoginFailed(tips);
    }

    @Test
    @Parameters(method = "collections") // 数据准备
    public void check(String phone, String password, boolean phoneExcepted, boolean passwordExcepted)
    {
        // 执行
        mPresenter.check(phone, password);
        // 验证
        verify(mView).changeInputState(phoneExcepted, passwordExcepted);
    }

    /**
     * 获取测试数据
     */
    private Collection<Object[]> collections()
    {
        return Arrays.asList(new Object[][]{ // 正常数据、错误数据、边界条件等
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

    @Test(expected = NullPointerException.class)// 验证
    public void onDestroy()
    {
        // 执行
        mPresenter.destroy();
        mPresenter.login("", "");
    }
}