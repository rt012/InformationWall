package wipraktikum.informationwallandroidapp.BusinessObject;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class Contact {
    private long mContactID;
    private String mSurname;
    private String mName;
    private String mEMailAddress;
    private String mTelephone;
    private String mCompany;
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
