package wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class DBBlackBoardAttachment {
    @DatabaseField(generatedId = true)
    private long mBlackBoardAttachmentID;
    @DatabaseField
    private String mAttachmentName;
    @DatabaseField
    private String mRemoteDataPath;
    @DatabaseField
    private String mDeviceDataPath;
    @DatabaseField
    private DataType mDataType = DataType.OTHER;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "mBlackBoardItemID")
    private DBBlackBoardItem mBlackBoardItem;
    @DatabaseField
    private boolean mSyncStatus;

    public DBBlackBoardAttachment(){};

    public DBBlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DataType mDataType, DBBlackBoardItem blackboardItem) {
        this.mRemoteDataPath = mRemoteDataPath;
        this.mDeviceDataPath = mDeviceDataPath;
        this.mDataType = mDataType;
        this.mBlackBoardItem = blackboardItem;
    }

    public long getBlackBoardAttachmentID() {
        return mBlackBoardAttachmentID;
    }

    public void setBlackBoardAttachmentID(long mBlackBoardAttachmentID) {
        this.mBlackBoardAttachmentID = mBlackBoardAttachmentID;
    }

    public DBBlackBoardItem getBlackBoardItem() {
        return mBlackBoardItem;
    }

    public void setBlackBoardItem(DBBlackBoardItem mBlackBoardItem) {
        this.mBlackBoardItem = mBlackBoardItem;
    }

    public String getAttachmentName() {
        return mAttachmentName;
    }

    public void setAttachmentName(String mName) {
        this.mAttachmentName = mName;
    }

    public String getDeviceDataPath() {
        return mDeviceDataPath;
    }

    public void setDeviceDataPath(String mDeviceDataPath) {
        this.mDeviceDataPath = mDeviceDataPath;
    }

    public String getRemoteDataPath() {
        return mRemoteDataPath;
    }

    public void setRemoteDataPath(String mRemoteDataPath) {
        this.mRemoteDataPath = mRemoteDataPath;
    }

    public DataType getDataType() {
        return mDataType;
    }

    public void setDataType(DataType mDataType) {
        this.mDataType = mDataType;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    public enum DataType {
        PDF, IMG, OTHER;
    }


}
