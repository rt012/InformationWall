package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable(tableName = "blackBoardItem")
public class BlackBoardItem {
    @DatabaseField(generatedId = true)
    private long mBlackBoardItemID;
    @DatabaseField
    private String mTitle;
    @DatabaseField(foreign = true)
    private Contact mContact;
    @DatabaseField
    private String mDescriptionText;
    @ForeignCollectionField(eager= true)
    private Collection<BlackBoardAttachment> mBlackBoardAttachment = new ArrayList<BlackBoardAttachment>();
    @DatabaseField
    private Date mCreatedTimestamp;
    @DatabaseField
    private Date mEditedTimestamp;

    BlackBoardItem(){};

    public BlackBoardItem(long mBlackBoardItemID, String mTitle, Contact mContact, String mDescriptionText, Collection<BlackBoardAttachment> mBlackBoardAttachment, Date mCreatedTimestamp, Date mEditedTimestamp) {
        this.mBlackBoardItemID = mBlackBoardItemID;
        this.mTitle = mTitle;
        this.mContact = mContact;
        this.mDescriptionText = mDescriptionText;
        this.mBlackBoardAttachment = mBlackBoardAttachment;
        this.mCreatedTimestamp = mCreatedTimestamp;
        this.mEditedTimestamp = mEditedTimestamp;
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

    public void setmBlackBoardAttachment(Collection<BlackBoardAttachment> attachments) throws SQLException {
        if (attachments == null) {
            Dao<BlackBoardItem, Long> dao = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO();
            this.mBlackBoardAttachment = dao.getEmptyForeignCollection("mBlackBoardAttachment");
        }
        this.mBlackBoardAttachment.addAll(attachments);
    }
}
