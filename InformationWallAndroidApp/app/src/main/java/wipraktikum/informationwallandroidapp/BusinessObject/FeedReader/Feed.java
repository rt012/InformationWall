package wipraktikum.informationwallandroidapp.BusinessObject.FeedReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * Created by Eric Schmidt on 21.12.2015.
 */
public class Feed {
    private long mFeedReaderID;
    private String mTitle;
    private String mWebsite;
    private String mDescription;
    private String mFeed;
    private String mImageURL;
    private boolean mSyncStatus;

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

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    public static Feed parseItemFromJson(String json) {
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gsonInstance.fromJson(new JsonParser().parse(json), Feed.class);
    }
}
