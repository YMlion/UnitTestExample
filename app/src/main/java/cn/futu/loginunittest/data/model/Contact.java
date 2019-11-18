package cn.futu.loginunittest.data.model;

/**
 * 联系人
 */
public class Contact
{
    private int id;
    private String name;
    private String phone;

    public Contact(int id, String name, String phone)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}
