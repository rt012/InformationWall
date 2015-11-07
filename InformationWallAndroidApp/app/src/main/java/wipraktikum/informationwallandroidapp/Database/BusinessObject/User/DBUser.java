package wipraktikum.informationwallandroidapp.Database.BusinessObject.User;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
@DatabaseTable
public class DBUser {
    @DatabaseField(generatedId = true)
    private long mUserID;
    @DatabaseField(unique = true)
    private String mEmailAddress;
    @DatabaseField
    private String mPassword;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private DBUserGroup mUserGroup;

    public DBUser(){}
}
