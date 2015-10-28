package wipraktikum.informationwallandroidapp.BusinessObject;

import com.j256.ormlite.table.DatabaseTable;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBTile;

/**
 * Created by Eric Schmidt on 21.10.2015.
 */
@DatabaseTable
public class Tile {
    private long mTileID;
    private String mName;
    private int mDrawableId;
    private String mScreen;
    private boolean mIsActivated = false;
    private DBTile.EnumTileSize mTileSize;

    public Tile(){}

    public Tile(String name, int drawableId, String screen) {
        this.mName = name;
        this.mDrawableId = drawableId;
        this.mScreen = screen;
        this.mTileSize = DBTile.EnumTileSize.SMALL;
    }

    public void setIsActivated(boolean isActivated){
        mIsActivated = isActivated;
    }

    public boolean getIsActivated(){
        return mIsActivated;
    }

    public DBTile.EnumTileSize getTileSize() {
        return mTileSize;
    }

    public void setTileSize(DBTile.EnumTileSize mTileSize) {
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

    public void setmTileID(long mTileID) {
        this.mTileID = mTileID;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmDrawableId(int mDrawableId) {
        this.mDrawableId = mDrawableId;
    }

    public void setmScreen(String mScreen) {
        this.mScreen = mScreen;
    }
}
