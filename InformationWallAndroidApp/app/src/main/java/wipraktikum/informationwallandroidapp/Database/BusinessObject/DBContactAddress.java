package wipraktikum.informationwallandroidapp.Database.BusinessObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class DBContactAddress {
    @DatabaseField(generatedId = true)
    private long mContactAddressID;
    @DatabaseField
    private String mStreetName;
    @DatabaseField
    private String mHouseNumber;
    @DatabaseField
    private String mZipCode;
    @DatabaseField
    private String mCity;
    @DatabaseField
    private boolean mSyncStatus;

    public DBContactAddress(){};

    public DBContactAddress(long mContactAddressID, String mStreetName, String mHouseNumber, String mZipCode, String mCity) {
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
        return mStreetName + " " + mHouseNumber + ", " + mCity;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }
}
