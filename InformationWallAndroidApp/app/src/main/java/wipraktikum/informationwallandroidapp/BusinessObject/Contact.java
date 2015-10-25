package wipraktikum.informationwallandroidapp.BusinessObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable(tableName = "contact")
public class Contact {
    Contact(){};

    public Contact(long mContactID, String mCompany, String mTelephone, String mEMailAddress, String mName, String mSurname, ContactAddress mContactAddress) {
        this.mContactAddress = mContactAddress;
        this.mCompany = mCompany;
        this.mTelephone = mTelephone;
        this.mEMailAddress = mEMailAddress;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mContactID = mContactID;
    }

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
    @DatabaseField
    public ContactAddress mContactAddress;
}
