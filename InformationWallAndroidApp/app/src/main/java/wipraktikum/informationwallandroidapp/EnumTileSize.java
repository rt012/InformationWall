package wipraktikum.informationwallandroidapp;

/**
 * Created by Eric Schmidt on 22.10.2015.
 */
public enum EnumTileSize {
    SMALL(0), MIDDLE(1), BIG(2);

    private final int mTileSizePos;

    EnumTileSize(int tileSizePos) {
        this.mTileSizePos = tileSizePos;
    }

    public int getValue() {
        return mTileSizePos;
    }

    public static EnumTileSize fromInt(int tileSizePos) {
        for (EnumTileSize enumTileSize : EnumTileSize .values()) {
            if (enumTileSize.getValue() == tileSizePos) { return enumTileSize; }
        }
        return null;
    }
}
