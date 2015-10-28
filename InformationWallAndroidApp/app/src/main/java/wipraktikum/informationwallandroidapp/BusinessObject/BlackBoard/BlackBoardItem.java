package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardItem {
    private long mBlackBoardItemID;
    private String mTitle;
    private Contact mContact;
    private String mDescriptionText;
    private ArrayList<BlackBoardAttachment> mBlackBoardAttachment;
    private Date mCreatedTimestamp;
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

    public ArrayList<BlackBoardAttachment> getmBlackBoardAttachment() {
        return mBlackBoardAttachment;
    }
}
