package wipraktikum.informationwallandroidapp.Database.BusinessObject.User;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
@DatabaseTable
public class DBUserGroup {
    @DatabaseField(generatedId = true)
    private long mUserGroupID;
    @DatabaseField
    private String mUserGroupName;
    @DatabaseField
    private boolean mEdit;
    @DatabaseField
    private boolean mWrite;
    @DatabaseField
    private boolean mRead;
    @DatabaseField
    private boolean mDelete;

    public DBUserGroup(){}
}
