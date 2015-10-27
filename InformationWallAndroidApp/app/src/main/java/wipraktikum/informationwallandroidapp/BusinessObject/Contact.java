package wipraktikum.informationwallandroidapp.BusinessObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class Contact {
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
    public ContactAddress mContactAddress;

    Contact(){};

    public Contact(String mName, String mSurname, String mEMailAddress,String mTelephone,  String mCompany, ContactAddress mContactAddress) {
        this.mContactAddress = mContactAddress;
        this.mCompany = mCompany;
        this.mTelephone = mTelephone;
        this.mEMailAddress = mEMailAddress;
        this.mName = mName;
        this.mSurname = mSurname;
    }

    public String getFullName(){
        return mSurname + ", " + mName;
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

    public ContactAddress getContactAddress() {
        return mContactAddress;
    }
}
