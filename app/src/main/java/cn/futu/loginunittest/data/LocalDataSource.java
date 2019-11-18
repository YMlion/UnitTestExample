package cn.futu.loginunittest.data;

import java.util.ArrayList;
import java.util.List;

import cn.futu.loginunittest.data.model.Contact;

/**
 * mock data source
 */
public class LocalDataSource
{

    public Result<List<Contact>> loadContact(String id)
    {
        final ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "小王", "18888888888"));
        return new Result.Success(contacts);
    }
}
