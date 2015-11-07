package wipraktikum.informationwallandroidapp.BusinessObject.User;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class UserGroup {
    private long mUserGroupID;
    private String mUserGroupName;
    private boolean mEdit;
    private boolean mWrite;
    private boolean mRead;
    private boolean mDelete;

    public UserGroup(){}

    public long getUserGroupID() {
        return mUserGroupID;
    }

    public void setUserGroupID(long mUserGroupID) {
        this.mUserGroupID = mUserGroupID;
    }

    public String getUserGroupName() {
        return mUserGroupName;
    }

    public void setUserGroupName(String mUserGroupName) {
        this.mUserGroupName = mUserGroupName;
    }

    public boolean isEdit() {
        return mEdit;
    }

    public void setEdit(boolean mEdit) {
        this.mEdit = mEdit;
    }

    public boolean isWrite() {
        return mWrite;
    }

    public void setWrite(boolean mWrite) {
        this.mWrite = mWrite;
    }

    public boolean isRead() {
        return mRead;
    }

    public void setRead(boolean mRead) {
        this.mRead = mRead;
    }

    public boolean isDelete() {
        return mDelete;
    }

    public void setDelete(boolean mDelete) {
        this.mDelete = mDelete;
    }
}
