package wipraktikum.informationwallandroidapp.BusinessObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable(tableName = "contactAddress")
public class ContactAdress {
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

    ContactAdress(){};

    public ContactAdress(long mContactAddressID, String mStreetName, String mHouseNumber, String mZipCode, String mCity) {
        this.mContactAddressID = mContactAddressID;
        this.mStreetName = mStreetName;
        this.mHouseNumber = mHouseNumber;
        this.mZipCode = mZipCode;
        this.mCity = mCity;
    }


}
