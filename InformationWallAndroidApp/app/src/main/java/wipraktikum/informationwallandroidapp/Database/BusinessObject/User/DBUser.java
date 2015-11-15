package wipraktikum.informationwallandroidapp.Database.BusinessObject.User;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
@DatabaseTable
public class DBUser {
    @DatabaseField(id = true)
    private long mUserID;
    @DatabaseField(unique = true)
    private String mEmailAddress;
    @DatabaseField
    private String mPassword;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DBUserGroup mUserGroup;
    @DatabaseField
    private boolean mLoggedIn;

    public DBUser(){}

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

    public DBUserGroup getUserGroup() {
        return mUserGroup;
    }

    public void setUserGroup(DBUserGroup mUserGroup) {
        this.mUserGroup = mUserGroup;
    }

    public boolean isLoggedIn() {
        return mLoggedIn;
    }

    public void setLoggedIn(boolean mLoggedIn) {
        this.mLoggedIn = mLoggedIn;
    }
}
