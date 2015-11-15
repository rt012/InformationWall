package wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.Contact.DBContact;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.User.DBUser;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class DBBlackBoardItem {
    @DatabaseField(id = true)
    private long mBlackBoardItemID;
    @DatabaseField
    private String mTitle;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DBContact mContact;
    @DatabaseField
    private String mDescriptionText;
    @ForeignCollectionField(eager= true)
    private ForeignCollection<DBBlackBoardAttachment> mBlackBoardAttachment;
    @DatabaseField
    private Date mCreatedTimestamp;
    @DatabaseField
    private Date mEditedTimestamp;
    @DatabaseField(columnName = SYNCSTATUS_FIELD_NAME)
    private boolean mSyncStatus;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DBUser mUser;

    public static final String SYNCSTATUS_FIELD_NAME = "syncStatus";

    public DBBlackBoardItem(){};

    public DBBlackBoardItem(String mTitle, DBContact mContact, String mDescriptionText, Date mCreatedTimestamp, Date mEditedTimestamp) {
        this.mTitle = mTitle;
        this.mContact = mContact;
        this.mDescriptionText = mDescriptionText;
        this.mCreatedTimestamp = mCreatedTimestamp;
        this.mEditedTimestamp = mEditedTimestamp;
    }

    public long getBlackBoardItemID() {
        return mBlackBoardItemID;
    }

    public void setBlackBoardItemID(long mBlackBoardItemID) {
        this.mBlackBoardItemID = mBlackBoardItemID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescriptionText() {
        return mDescriptionText;
    }

    public void setDescriptionText(String mDescriptionText) {
        this.mDescriptionText = mDescriptionText;
    }

    public Date getCreatedTimestamp() {
        return mCreatedTimestamp;
    }

    public void setCreatedTimestamp(Date mCreatedTimestamp) {
        this.mCreatedTimestamp = mCreatedTimestamp;
    }

    public Date getEditedTimestamp() {
        return mEditedTimestamp;
    }

    public void setEditedTimestamp(Date mEditedTimestamp) {
        this.mEditedTimestamp = mEditedTimestamp;
    }

    public DBContact getContact() {
        return mContact;
    }

    public void setContact(DBContact mContact) {
        this.mContact = mContact;
    }

    public List<DBBlackBoardAttachment> getBlackBoardAttachment() {
        List<DBBlackBoardAttachment> blackBoardAttachment = new ArrayList<DBBlackBoardAttachment>();
        for (DBBlackBoardAttachment note : mBlackBoardAttachment) {
            blackBoardAttachment.add(note);
        }
        return blackBoardAttachment;
    }

    public void setBlackBoardAttachment(List<DBBlackBoardAttachment> attachments) throws SQLException {
        if (attachments == null) {
            Dao<DBBlackBoardItem, Long> blackBoardItemsDAO = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO();
            this.mBlackBoardAttachment = blackBoardItemsDAO.getEmptyForeignCollection("mBlackBoardAttachment");
        }
        this.mBlackBoardAttachment.addAll(attachments);
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    public DBUser getUser() {
        return mUser;
    }

    public void setUser(DBUser mUser) {
        this.mUser = mUser;
    }
}
