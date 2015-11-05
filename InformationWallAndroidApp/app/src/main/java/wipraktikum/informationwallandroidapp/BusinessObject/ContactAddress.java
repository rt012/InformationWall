package wipraktikum.informationwallandroidapp.BusinessObject;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class ContactAddress {
    private long mContactAddressID;
    private String mStreetName;
    private String mHouseNumber;
    private String mZipCode;
    private String mCity;
    private transient boolean mSyncStatus;

    public ContactAddress(){};

    public ContactAddress(long mContactAddressID, String mStreetName, String mHouseNumber, String mZipCode, String mCity) {
        this.mContactAddressID = mContactAddressID;
        this.mStreetName = mStreetName;
        this.mHouseNumber = mHouseNumber;
        this.mZipCode = mZipCode;
        this.mCity = mCity;
    }

    public long getContactAddressID() {
        return mContactAddressID;
    }

    public void setContactAddressID(long mContactAddressID) {
        this.mContactAddressID = mContactAddressID;
    }

    public String getStreetName() {
        return mStreetName;
    }

    public void setStreetName(String mStreetName) {
        this.mStreetName = mStreetName;
    }

    public String getHouseNumber() {
        return mHouseNumber;
    }

    public void setHouseNumber(String mHouseNumber) {
        this.mHouseNumber = mHouseNumber;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getFullAddress(){
        if (mStreetName != null && mHouseNumber != null && mCity != null) {
            return mStreetName + " " + mHouseNumber + ", " + mCity;
        }else if (mStreetName == null && mCity != null){
            return mCity;
        }else if (mStreetName != null && mHouseNumber == null && mCity != null){
            return mStreetName + ", " + mCity;
        }

        return null;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }
}
