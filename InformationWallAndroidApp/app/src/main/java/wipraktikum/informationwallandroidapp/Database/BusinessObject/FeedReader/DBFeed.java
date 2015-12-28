package wipraktikum.informationwallandroidapp.Database.BusinessObject.FeedReader;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
@DatabaseTable
public class DBFeed {
    @DatabaseField(id = true)
    private long mFeedReaderID;
    @DatabaseField
    private String mTitle;
    @DatabaseField
    private String mWebsite;
    @DatabaseField
    private String mDescription;
    @DatabaseField
    private String mFeed;
    @DatabaseField
    private String mImageURL;
    @DatabaseField(columnName = SYNCSTATUS_FIELD_NAME)
    private boolean mSyncStatus;

    public static final String SYNCSTATUS_FIELD_NAME = "syncStatus";

    public long getFeedReaderID() {
        return mFeedReaderID;
    }

    public void setFeedReaderID(long mFeedReaderID) {
        this.mFeedReaderID = mFeedReaderID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getFeed() {
        return mFeed;
    }

    public void setFeed(String mFeed) {
        this.mFeed = mFeed;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }
}
