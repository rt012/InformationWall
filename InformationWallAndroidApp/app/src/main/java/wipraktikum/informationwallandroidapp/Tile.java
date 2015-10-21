package wipraktikum.informationwallandroidapp;

/**
 * Created by Eric Schmidt on 21.10.2015.
 */
public class Tile {
    public final String mName;
    public final int mDrawableId;
    public final Class mScreen;
    public boolean mIsActivated = false;

    Tile(String name, int drawableId, Class screen) {
        this.mName = name;
        this.mDrawableId = drawableId;
        this.mScreen = screen;
    }

    public void setIsActivated(boolean isActivated){
        mIsActivated = isActivated;
    }

    public boolean getIsActivated(){
        return mIsActivated;
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
