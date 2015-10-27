package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardAttachment {
    @DatabaseField(generatedId = true)
    private long mBlackBoardAttachmentID;
    @DatabaseField
    private String mRemoteDataPath;
    @DatabaseField
    private String mDeviceDataPath;
    @DatabaseField
    private DataType mDataType;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "mBlackBoardItemID")
    private BlackBoardItem mBlackBoardItem;

    BlackBoardAttachment(){};

    public BlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DataType mDataType) {
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
