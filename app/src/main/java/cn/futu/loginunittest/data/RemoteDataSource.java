package cn.futu.loginunittest.data;

import java.util.ArrayList;
import java.util.List;

import cn.futu.loginunittest.data.model.Contact;
import cn.futu.loginunittest.data.model.PhoneInfo;
import cn.futu.loginunittest.data.model.User;

/**
 * mock data source
 */
public class RemoteDataSource
{

    public Result<User> login(String phone, String password)
    {

        if ("13312345678".equals(phone))
        {
            if ("123456".equals(password))
            {
                return new Result.Success<>(new User("000001", "张三", true));
            }
            else
            {
                return new Result.Error(new Exception("密码错误"));
            }
        }
        else if ("13333333333".equals(phone))
        {
            if ("123456".equals(password))
            {
                return new Result.Success<>(new User("000002", "李四", false));
            }
            else
            {
                return new Result.Error(new Exception("密码错误"));
            }
        }
        else
        {
            return new Result.Error(new Exception("用户未注册"));
        }
    }

    public Result<List<Contact>> loadContact(String id)
    {
        final ArrayList<Contact> contacts = new ArrayList<>();
        if ("000002".equals(id))
        {
            contacts.add(new Contact(1, "张三", "13312345678"));
            contacts.add(new Contact(2, "李四", "18888888888"));
        }
        return new Result.Success(contacts);
    }

    public Result searchPhone(String phone)
    {
        if ("110".equals(phone))
        {
            final PhoneInfo phoneInfo = new PhoneInfo(phone, true);
            phoneInfo.setUsage("报警电话");
            return new Result.Success<>(phoneInfo);
        }
        else if ("16666666666".equals(phone))
        {
            final PhoneInfo phoneInfo = new PhoneInfo(phone, false);
            phoneInfo.setAddress("无");
            phoneInfo.setState("违法电话");
            return new Result.Success<>(phoneInfo);
        }
        else if ("18888888888".equals(phone))
        {
            final PhoneInfo phoneInfo = new PhoneInfo(phone, false);
            phoneInfo.setAddress("北京");
            phoneInfo.setState("正常");
            return new Result.Success<>(phoneInfo);
        }

        return new Result.Success<>("未找到该电话信息");
    }
}
