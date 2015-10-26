package wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
@DatabaseTable(tableName = "blackBoardAttachment")
public class BlackBoardAttachment {
    @DatabaseField(generatedId = true)
    private long mBlackBoardAttachmentID;
    @DatabaseField
    private String mRemoteDataPath;
    @DatabaseField
    private String mDeviceDataPath;
    @DatabaseField
    private String mDataType;
    @DatabaseField(canBeNull = true, foreign = true)
    private BlackBoardItem mBlackBoardItem;

    BlackBoardAttachment(){};

    public BlackBoardAttachment(long mBlackBoardAttachmentID, String mRemoteDataPath, String mDeviceDataPath, String mDataType) {
        this.mBlackBoardAttachmentID = mBlackBoardAttachmentID;
        this.mRemoteDataPath = mRemoteDataPath;
        this.mDeviceDataPath = mDeviceDataPath;
        this.mDataType = mDataType;
    }
}
