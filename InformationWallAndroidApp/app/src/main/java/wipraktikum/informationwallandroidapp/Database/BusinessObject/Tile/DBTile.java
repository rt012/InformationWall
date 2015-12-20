package wipraktikum.informationwallandroidapp.Database.BusinessObject.Tile;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 21.10.2015.
 */
@DatabaseTable
public class DBTile {
    @DatabaseField(generatedId = true)
    private long mTileID;
    @DatabaseField(unique = true, columnName = NAME_FIELD_NAME)
    private String mName;
    @DatabaseField
    private int mDrawableId;
    @DatabaseField
    private String mScreen;
    @DatabaseField
    private boolean mIsActivated = false;
    @DatabaseField
    private EnumTileSize mTileSize;
    @DatabaseField
    private boolean mSyncStatus;

    public static final String NAME_FIELD_NAME = "Name";

    public DBTile(){}

    public DBTile(String name, int drawableId, String screen) {
        this.mName = name;
        this.mDrawableId = drawableId;
        this.mScreen = screen;
        this.mTileSize = EnumTileSize.SMALL;
    }

    public long getTileID() {
        return mTileID;
    }

    public void setTileID(long mTileID) {
        this.mTileID = mTileID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int mDrawableId) {
        this.mDrawableId = mDrawableId;
    }

    public String getScreen() {
        return mScreen;
    }

    public void setScreen(String mScreen) {
        this.mScreen = mScreen;
    }

    public boolean isIsActivated() {
        return mIsActivated;
    }

    public void setIsActivated(boolean mIsActivated) {
        this.mIsActivated = mIsActivated;
    }

    public EnumTileSize getTileSize() {
        return mTileSize;
    }

    public void setTileSize(EnumTileSize mTileSize) {
        this.mTileSize = mTileSize;
    }

    public boolean getIsActivated(){
        return mIsActivated;
    }

    public boolean isSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(boolean mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    public enum EnumTileSize {
        SMALL, MIDDLE, BIG;
    }
}
