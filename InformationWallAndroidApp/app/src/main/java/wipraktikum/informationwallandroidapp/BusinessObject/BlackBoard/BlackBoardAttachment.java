package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.table.DatabaseTable;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardAttachment {
    private long mBlackBoardAttachmentID;
    private String mRemoteDataPath;
    private String mDeviceDataPath;
    private DBBlackBoardAttachment.DataType mDataType;

    public BlackBoardAttachment(){}

    public BlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DBBlackBoardAttachment.DataType mDataType, BlackBoardItem blackboardItem) {
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
}
