package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable
public class BlackBoardAttachment {
    @Expose
    @DatabaseField(generatedId = true)
    private long mBlackBoardAttachmentID;
    @Expose
    @DatabaseField
    private String mRemoteDataPath;
    @Expose
    @DatabaseField
    private String mDeviceDataPath;
    @Expose
    @DatabaseField
    private DataType mDataType;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "mBlackBoardItemID")
    private BlackBoardItem mBlackBoardItem;

    BlackBoardAttachment(){};

    public BlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DataType mDataType, BlackBoardItem blackboardItem) {
        this.mRemoteDataPath = mRemoteDataPath;
        this.mDeviceDataPath = mDeviceDataPath;
        this.mDataType = mDataType;
        this.mBlackBoardItem = blackboardItem;
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
