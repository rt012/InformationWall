package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardItem {
    @DatabaseField(generatedId = true)
    private long mBlackBoardItemID;
    @DatabaseField
    private String mTitle;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Contact mContact;
    @DatabaseField
    private String mDescriptionText;
    @ForeignCollectionField(eager= true)
    private ForeignCollection<BlackBoardAttachment> mBlackBoardAttachment;
    @DatabaseField
    private Date mCreatedTimestamp;
    @DatabaseField
    private Date mEditedTimestamp;

    BlackBoardItem(){};

    public BlackBoardItem(String mTitle, Contact mContact, String mDescriptionText, Date mCreatedTimestamp, Date mEditedTimestamp) {
        this.mTitle = mTitle;
        this.mContact = mContact;
        this.mDescriptionText = mDescriptionText;
        this.mCreatedTimestamp = mCreatedTimestamp;
        this.mEditedTimestamp = mEditedTimestamp;
    }

    public long getBlackBoardItemID() {
        return mBlackBoardItemID;
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

    public void setCreatedTimestamp(Date CreatedTimestamp) {
        this.mCreatedTimestamp = mCreatedTimestamp;
    }

    public Contact getContact() {
        return mContact;
    }

    public void setContact(Contact mContact) {
        this.mContact = mContact;
    }

    public List<BlackBoardAttachment> getBlackBoardAttachment() {
        List<BlackBoardAttachment> blackBoardAttachment = new ArrayList<BlackBoardAttachment>();
        for (BlackBoardAttachment note : mBlackBoardAttachment) {
            blackBoardAttachment.add(note);
        }
        return blackBoardAttachment;
    }

    public void setBlackBoardAttachment(List<BlackBoardAttachment> attachments) throws SQLException {
        if (attachments == null) {
            Dao<BlackBoardItem, Long> blackBoardItemsDAO = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO();
            this.mBlackBoardAttachment = blackBoardItemsDAO.getEmptyForeignCollection("mBlackBoardAttachment");
        }
        this.mBlackBoardAttachment.addAll(attachments);
    }
}
