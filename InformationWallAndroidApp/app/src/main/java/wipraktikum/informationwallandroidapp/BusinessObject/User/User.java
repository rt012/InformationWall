package wipraktikum.informationwallandroidapp.BusinessObject.User;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class User {
    private long mUserID;
    private String mEmailAddress;
    private String mPassword;
    private UserGroup mUserGroup;
    private transient boolean mLoggedIn;

    public User(){}

    public User(String mEmailAddress, String mPassword) {
        this.mEmailAddress = mEmailAddress;
        this.mPassword = mPassword;
    }

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

    public boolean isLoggedIn() {
        return mLoggedIn;
    }

    public void setLoggedIn(boolean mLoggedIn) {
        this.mLoggedIn = mLoggedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this.getUserID()== ((User) o).getUserID()) {
            return true;
        }
        return false;
    }
}
