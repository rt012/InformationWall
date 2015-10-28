package wipraktikum.informationwallandroidapp.Database.BusinessObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class DBContact {
    @DatabaseField(generatedId = true)
    private long mContactID;
    @DatabaseField
    private String mSurname;
    @DatabaseField
    private String mName;
    @DatabaseField
    private String mEMailAddress;
    @DatabaseField
    private String mTelephone;
    @DatabaseField
    private String mCompany;
    @DatabaseField(foreign = true,  foreignAutoCreate = true, foreignAutoRefresh = true)
    public DBContactAddress mContactAddress;

    public DBContact(){};

    public DBContact(String mName, String mSurname, String mEMailAddress, String mTelephone, String mCompany, DBContactAddress mContactAddress) {
        this.mContactAddress = mContactAddress;
        this.mCompany = mCompany;
        this.mTelephone = mTelephone;
        this.mEMailAddress = mEMailAddress;
        this.mName = mName;
        this.mSurname = mSurname;
    }

    public long getmContactID() {
        return mContactID;
    }

    public String getFullName(){
        return mSurname + ", " + mName;
    }

    public String getSurname() {
        return mSurname;
    }

    public String getName() {
        return mName;
    }

    public String getEMailAddress() {
        return mEMailAddress;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public String getCompany() {
        return mCompany;
    }

    public DBContactAddress getContactAddress() {
        return mContactAddress;
    }

    public void setContactID(long mContactID) {
        this.mContactID = mContactID;
    }

    public void setSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setEMailAddress(String mEMailAddress) {
        this.mEMailAddress = mEMailAddress;
    }

    public void setTelephone(String mTelephone) {
        this.mTelephone = mTelephone;
    }

    public void setCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public void setContactAddress(DBContactAddress mContactAddress) {
        this.mContactAddress = mContactAddress;
    }
}
