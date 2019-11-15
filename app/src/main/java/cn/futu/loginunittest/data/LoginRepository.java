package cn.futu.loginunittest.data;

import cn.futu.loginunittest.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository
{

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource)
    {
        if (instance == null)
        {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public Result<LoggedInUser> login(String username, String password)
    {
        // handle login
        return dataSource.login(username, password);
    }
}
