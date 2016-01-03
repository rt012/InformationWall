package wipraktikum.informationwallandroidapp.BusinessObject.Contact;

import com.j256.ormlite.table.DatabaseTable;

import wipraktikum.informationwallandroidapp.Utils.StringHelper;

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
        if (!StringHelper.isStringNullOrEmpty(mStreetName) && !StringHelper.isStringNullOrEmpty(mHouseNumber)
                && !StringHelper.isStringNullOrEmpty(mCity)) {
            return mStreetName + " " + mHouseNumber + ", " + mCity;
        }else if (StringHelper.isStringNullOrEmpty(mStreetName) && !StringHelper.isStringNullOrEmpty(mCity)){
            return mCity;
        }else if (!StringHelper.isStringNullOrEmpty(mStreetName) && StringHelper.isStringNullOrEmpty(mHouseNumber)
                && !StringHelper.isStringNullOrEmpty(mCity)){
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

    public boolean isEmpty(){
        if (StringHelper.isStringNullOrEmpty(getFullAddress())) return true;
        return false;
    }

    public static void splitStreet(ContactAddress contactAddress, String street) {
        String[] splitStreet = street.split(" ");
        if (splitStreet.length == 2) {
            contactAddress.setStreetName(splitStreet[0]);
            contactAddress.setHouseNumber(splitStreet[1]);
        } else {
            contactAddress.setStreetName(splitStreet[0]);
        }
    }

    public static void splitCity(ContactAddress contactAddress, String city) {
        String[] splitCity = city.split(" ");
        if (splitCity.length == 2) {
            contactAddress.setZipCode(splitCity[0]);
            contactAddress.setCity(splitCity[1]);
        } else {
            contactAddress.setCity(splitCity[0]);
        }
    }
}
