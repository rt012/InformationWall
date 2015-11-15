package wipraktikum.informationwallandroidapp.Database.BusinessObject.User;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
@DatabaseTable
public class DBUserGroup {
    @DatabaseField(id = true)
    private long mUserGroupID;
    @DatabaseField
    private String mUserGroupName;
    @DatabaseField
    private boolean mEdit;
    @DatabaseField
    private boolean mWrite;
    @DatabaseField
    private boolean mRead;
    @DatabaseField
    private boolean mDelete;

    public DBUserGroup(){}

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

    public boolean canEdit() {
        return mEdit;
    }

    public void setEdit(boolean mEdit) {
        this.mEdit = mEdit;
    }

    public boolean canWrite() {
        return mWrite;
    }

    public void setWrite(boolean mWrite) {
        this.mWrite = mWrite;
    }

    public boolean canRead() {
        return mRead;
    }

    public void setRead(boolean mRead) {
        this.mRead = mRead;
    }

    public boolean canDelete() {
        return mDelete;
    }

    public void setDelete(boolean mDelete) {
        this.mDelete = mDelete;
    }
}
