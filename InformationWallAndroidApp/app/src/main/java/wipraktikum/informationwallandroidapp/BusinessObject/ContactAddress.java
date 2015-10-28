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

    public ContactAddress(){};

    public ContactAddress(long mContactAddressID, String mStreetName, String mHouseNumber, String mZipCode, String mCity) {
        this.mContactAddressID = mContactAddressID;
        this.mStreetName = mStreetName;
        this.mHouseNumber = mHouseNumber;
        this.mZipCode = mZipCode;
        this.mCity = mCity;
    }

    public long getmContactAddressID() {
        return mContactAddressID;
    }

    public void setmContactAddressID(long mContactAddressID) {
        this.mContactAddressID = mContactAddressID;
    }

    public String getmStreetName() {
        return mStreetName;
    }

    public void setmStreetName(String mStreetName) {
        this.mStreetName = mStreetName;
    }

    public String getmHouseNumber() {
        return mHouseNumber;
    }

    public void setmHouseNumber(String mHouseNumber) {
        this.mHouseNumber = mHouseNumber;
    }

    public String getmZipCode() {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getFullAddress(){
        return mStreetName + " " + mHouseNumber + ", " + mCity;
    }

}
