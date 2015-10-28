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
    private String mRemoteDataPath;
    @DatabaseField
    private String mDeviceDataPath;
    @DatabaseField
    private DataType mDataType;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "mBlackBoardItemID")
    private DBBlackBoardItem mBlackBoardItem;

    DBBlackBoardAttachment(){};

    public DBBlackBoardAttachment(String mRemoteDataPath, String mDeviceDataPath, DataType mDataType, DBBlackBoardItem blackboardItem) {
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
