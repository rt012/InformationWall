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
    @DatabaseField(columnName = LOGGED_IN_FIELD_NAME)
    private boolean mLoggedIn;
    @DatabaseField(columnName = PREVIOUS_LOGGED_IN_FIELD_NAME)
    private boolean mPreviousLoggedIn;
    @DatabaseField
    private boolean mKeepLogInData;

    public static final String PREVIOUS_LOGGED_IN_FIELD_NAME = "previousLoggedIn";
    public static final String LOGGED_IN_FIELD_NAME = "loggedIn";

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

    public boolean isPreviousLoggedIn() {
        return mPreviousLoggedIn;
    }

    public void setPreviousLoggedIn(boolean previousLoggedIn) {
        this.mPreviousLoggedIn = previousLoggedIn;
    }

    public boolean isKeepLogInData() {
        return mKeepLogInData;
    }

    public void setKeepLogInData(boolean mKeepLogInData) {
        this.mKeepLogInData = mKeepLogInData;
    }
}
