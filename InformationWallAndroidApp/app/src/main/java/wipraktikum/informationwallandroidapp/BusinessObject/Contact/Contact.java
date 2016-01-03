package wipraktikum.informationwallandroidapp.BusinessObject.Contact;

import com.j256.ormlite.table.DatabaseTable;

import wipraktikum.informationwallandroidapp.Utils.StringHelper;

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
    private transient boolean mSyncStatus;

    public Contact(){};

    public Contact(String mName, String mSurname, String mEMailAddress,String mTelephone,  String mCompany, ContactAddress mContactAddress) {
        this.mContactAddress = mContactAddress;
        this.mCompany = mCompany;
        this.mTelephone = mTelephone;
        this.mEMailAddress = mEMailAddress;
        this.mName = mName;
        this.mSurname = mSurname;
    }

    public long getContactID() {
        return mContactID;
    }

    public String getFullName(){
        if (!StringHelper.isStringNullOrEmpty(mName) && !StringHelper.isStringNullOrEmpty(mSurname)) {
            return mSurname + ", " + mName;
        }else if (!StringHelper.isStringNullOrEmpty(mSurname)){
            return mSurname;
        }
        return null;
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

    public ContactAddress getContactAddress() {
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

    public void setContactAddress(ContactAddress mContactAddress) {
        this.mContactAddress = mContactAddress;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    @Override
    public boolean equals(Object o){
        if (o != null) {
            if (((Contact) o).getEMailAddress().equals(this.getEMailAddress())) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        if (StringHelper.isStringNullOrEmpty(getCompany()) && StringHelper.isStringNullOrEmpty(getEMailAddress())
                && StringHelper.isStringNullOrEmpty(getFullName()) && StringHelper.isStringNullOrEmpty(getTelephone())
                && getContactAddress().isEmpty()) return true;
        return false;
    }

    public static void splitContactFullName(Contact contact, String fullName) {
        String[] splitFullName = fullName.split(" ");
        if (splitFullName.length == 2) {
            contact.setSurname(splitFullName[0]);
            contact.setName(splitFullName[1]);
        } else {
            contact.setSurname(splitFullName[0]);
        }
    }
}
