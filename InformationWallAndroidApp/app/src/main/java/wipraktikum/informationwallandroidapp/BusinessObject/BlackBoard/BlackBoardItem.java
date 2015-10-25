package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable(tableName = "blackBoardItem")
public class BlackBoardItem {
    @DatabaseField(generatedId = true)
    private long mBlackBoardItemID;
    @DatabaseField
    private String mTitle;
    @DatabaseField
    private Contact mContact;
    @DatabaseField
    private String mDescriptionText;
    @DatabaseField (dataType= DataType.SERIALIZABLE)
    private ArrayList<BlackBoardAttachment> mBlackBoardAttachment;
    @DatabaseField
    private Date mCreatedTimestamp;
    @DatabaseField
    private Date mEditedTimestamp;

    BlackBoardItem(){};

    public BlackBoardItem(long mBlackBoardItemID, String mTitle, Contact mContact, String mDescriptionText, ArrayList<BlackBoardAttachment> mBlackBoardAttachment, Date mCreatedTimestamp, Date mEditedTimestamp) {
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
}