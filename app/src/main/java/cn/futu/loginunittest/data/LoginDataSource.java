package cn.futu.loginunittest.data;

import java.io.IOException;

import cn.futu.loginunittest.data.model.LoggedInUser;

/**
 * mock data source
 */
public class LoginDataSource
{

    public Result<LoggedInUser> login(String username, String password)
    {

        try
        {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username, false);
            return new Result.Success<>(fakeUser);
        }
        catch (Exception e)
        {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}
