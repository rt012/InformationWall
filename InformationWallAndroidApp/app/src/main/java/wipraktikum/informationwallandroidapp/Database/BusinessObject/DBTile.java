package wipraktikum.informationwallandroidapp.Database.BusinessObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Eric Schmidt on 21.10.2015.
 */
@DatabaseTable
public class DBTile {
    @DatabaseField(generatedId = true)
    private long mTileID;
    @DatabaseField(unique = true)
    private String mName;
    @DatabaseField
    private int mDrawableId;
    @DatabaseField
    private String mScreen;
    @DatabaseField
    private boolean mIsActivated = false;
    @DatabaseField
    private EnumTileSize mTileSize;

    DBTile(){}

    public DBTile(String name, int drawableId, String screen) {
        this.mName = name;
        this.mDrawableId = drawableId;
        this.mScreen = screen;
        this.mTileSize = EnumTileSize.SMALL;
    }

    public void setIsActivated(boolean isActivated){
        mIsActivated = isActivated;
    }

    public boolean getIsActivated(){
        return mIsActivated;
    }

    public EnumTileSize getTileSize() {
        return mTileSize;
    }

    public void setTileSize(EnumTileSize mTileSize) {
        this.mTileSize = mTileSize;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public String getScreen() {
        return mScreen;
    }

    public String getName() {
        return mName;
    }

    public long getTileID() {
        return mTileID;
    }

    public enum EnumTileSize {
        SMALL, MIDDLE, BIG;
    }
}
