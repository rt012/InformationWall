package wipraktikum.informationwallandroidapp;

/**
 * Created by Eric Schmidt on 21.10.2015.
 */
public class Tile {
    private final String mName;
    private final int mDrawableId;
    private final Class mScreen;
    private boolean mIsActivated = false;
    private EnumTileSize mTileSize;

    Tile(String name, int drawableId, Class screen) {
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

    public Class getScreen() {
        return mScreen;
    }

    public String getName() {
        return mName;
    }
}
