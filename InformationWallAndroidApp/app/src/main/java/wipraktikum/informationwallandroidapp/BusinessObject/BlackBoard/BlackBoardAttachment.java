package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardAttachment {
    private long mBlackBoardAttachmentID;
    private String mRemoteDataPath;
    private String mDeviceDataPath;
    private DataType mDataType;

    public BlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DataType mDataType, BlackBoardItem blackboardItem) {
        this.mRemoteDataPath = mRemoteDataPath;
        this.mDeviceDataPath = mDeviceDataPath;
        this.mDataType = mDataType;
    }

    public String getDeviceDataPath() {
        return mDeviceDataPath;
    }

    public String getRemoteDataPath() {
        return mRemoteDataPath;
    }

    public DataType getDataType() {
        return mDataType;
    }

    public enum DataType {
        PDF, IMG, OTHER;
    }
}
