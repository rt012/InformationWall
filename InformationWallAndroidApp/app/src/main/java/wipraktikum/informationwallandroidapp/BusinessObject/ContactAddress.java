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

    ContactAddress(){};

    public ContactAddress(long mContactAddressID, String mStreetName, String mHouseNumber, String mZipCode, String mCity) {
        this.mContactAddressID = mContactAddressID;
        this.mStreetName = mStreetName;
        this.mHouseNumber = mHouseNumber;
        this.mZipCode = mZipCode;
        this.mCity = mCity;
    }

    public String getFullAddress(){
        return mStreetName + " " + mHouseNumber + ", " + mCity;
    }

}
