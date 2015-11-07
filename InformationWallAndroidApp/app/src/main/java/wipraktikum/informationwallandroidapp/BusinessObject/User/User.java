package wipraktikum.informationwallandroidapp.BusinessObject.User;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class User {
    private long mUserID;
    private String mEmailAddress;
    private String mPassword;
    private UserGroup mUserGroup;

    public User(){}

    public long getUserID() {
        return mUserID;
    }

    public void setUserID(long mUserID) {
        this.mUserID = mUserID;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public UserGroup getUserGroup() {
        return mUserGroup;
    }

    public void setUserGroup(UserGroup mUserGroup) {
        this.mUserGroup = mUserGroup;
    }
}
