package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardItem{
    private long mBlackBoardItemID;
    private String mTitle;
    private Contact mContact;
    private String mDescriptionText;
    private List<BlackBoardAttachment> mBlackBoardAttachment;
    private Date mCreatedTimestamp;
    private Date mEditedTimestamp;
    private transient boolean mSyncStatus;
    private User mUser;

    public BlackBoardItem(){};

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

    public Contact getContact() {
        return mContact;
    }

    public void setContact(Contact mContact) {
        this.mContact = mContact;
    }

    public List<BlackBoardAttachment> getBlackBoardAttachment() {
        return mBlackBoardAttachment;
    }

    public void setBlackBoardAttachment(List<BlackBoardAttachment> mBlackBoardAttachment) {
        this.mBlackBoardAttachment = mBlackBoardAttachment;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    @Override
    public boolean equals(Object o) {
        BlackBoardItem compareBlackBoardItem = (BlackBoardItem) o;
        if(this.getBlackBoardItemID() == compareBlackBoardItem.getBlackBoardItemID()) {
            return true;
        }
        return false;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
