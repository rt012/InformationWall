package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.table.DatabaseTable;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardAttachment {
    private long mBlackBoardAttachmentID;
    private String mName;
    private String mRemoteDataPath;
    private String mDeviceDataPath;
    private DBBlackBoardAttachment.DataType mDataType = DBBlackBoardAttachment.DataType.OTHER;
    private transient DBBlackBoardItem mDBBlackBoardItem;
    private transient boolean mSyncStatus;

    public BlackBoardAttachment(){}

    public BlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DBBlackBoardAttachment.DataType mDataType, DBBlackBoardItem mDBBlackBoardItem) {
        this.mRemoteDataPath = mRemoteDataPath;
        this.mDeviceDataPath = mDeviceDataPath;
        this.mDataType = mDataType;
    }

    public long getBlackBoardAttachmentID() {
        return mBlackBoardAttachmentID;
    }

    public void setBlackBoardAttachmentID(long mBlackBoardAttachmentID) {
        this.mBlackBoardAttachmentID = mBlackBoardAttachmentID;
    }

    public DBBlackBoardItem getBlackBoardItem() {
        return mDBBlackBoardItem;
    }

    public void setBlackBoardItem(DBBlackBoardItem mDBBlackBoardItem) {
        this.mDBBlackBoardItem = mDBBlackBoardItem;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
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

    public DBBlackBoardAttachment.DataType getDataType() {
        return mDataType;
    }

    public void setDataType(DBBlackBoardAttachment.DataType mDataType) {
        this.mDataType = mDataType;
    }

    @Override
    public boolean equals(Object o) {
        BlackBoardAttachment compareBlackBoardAttachment = (BlackBoardAttachment) o;
        if (this.getRemoteDataPath() != null) {
            if (this.getRemoteDataPath().equals(compareBlackBoardAttachment.getRemoteDataPath())) {
                return true;
            }
        }else{
            if (this.getDeviceDataPath().equals(compareBlackBoardAttachment.getDeviceDataPath())) {
                return true;
            }
        }
        return false;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }
}
