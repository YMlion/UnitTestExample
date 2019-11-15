package cn.futu.loginunittest.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser
{

    private String userId;
    private String displayName;
    private boolean isVerifyPhoneCode;

    public LoggedInUser(String userId, String displayName, boolean isVerifyPhoneCode)
    {
        this.userId = userId;
        this.displayName = displayName;
        this.isVerifyPhoneCode = isVerifyPhoneCode;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public boolean isVerifyPhoneCode()
    {
        return isVerifyPhoneCode;
    }
}
