package cn.futu.loginunittest.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.User;

/**
 * mock data source
 */
public class RemoteDataSource
{

    public Result<User> login(String username, String password)
    {

        try
        {
            User fakeUser =
                    new User(
                            java.util.UUID.randomUUID().toString(),
                            username, false);
            return new Result.Success<>(fakeUser);
        }
        catch (Exception e)
        {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<List<Contact>> loadContact(String id)
    {
        final ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "小王", "18888888888"));
        return new Result.Success(contacts);
    }
}
