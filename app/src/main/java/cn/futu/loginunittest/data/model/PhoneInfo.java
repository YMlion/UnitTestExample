package cn.futu.loginunittest.data.model;

/**
 * phone info
 */
public class PhoneInfo
{
    private String phoneNum;
    private boolean isPublic;
    private String usage;
    private String address;
    private String state;

    public PhoneInfo(String phoneNum, boolean isPublic)
    {
        this.phoneNum = phoneNum;
        this.isPublic = isPublic;
    }

    public String getUsage()
    {
        return usage;
    }

    public void setUsage(String usage)
    {
        this.usage = usage;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public void setPublic(boolean aPublic)
    {
        isPublic = aPublic;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public static class NotFoundException extends Exception
    {
        public NotFoundException(String s)
        {
            super(s);
        }
    }
}
