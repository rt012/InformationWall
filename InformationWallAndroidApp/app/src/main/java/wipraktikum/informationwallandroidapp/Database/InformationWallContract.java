package wipraktikum.informationwallandroidapp.Database;

import android.provider.BaseColumns;

/**
 * Created by Eric Schmidt on 22.10.2015.
 */
public class InformationWallContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Settings.TABLE_NAME + " (" +
                    Settings._ID + " INTEGER PRIMARY KEY," +
                    Settings.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    Settings.COLUMN_TILE_SIZE_ID + TEXT_TYPE + COMMA_SEP +
                    Settings.COLUMN_IS_ACTIVATED + TEXT_TYPE + COMMA_SEP +
                    Settings.COLUMN_DRAWABLE_ID + TEXT_TYPE + COMMA_SEP +
                    Settings.COLUMN_SCREEN_NAME + TEXT_TYPE + COMMA_SEP + " )" +
                    "CREATE TABLE " + Tiles.TABLE_NAME + " (" +
                    Tiles._ID + " INTEGER PRIMARY KEY," +
                    Tiles.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    Tiles.COLUMN_TILE_NAME + TEXT_TYPE + COMMA_SEP +
                    Tiles.COLUMN_SETTINGS_ID + TEXT_TYPE + COMMA_SEP + " )" +
                    "CREATE TABLE " + TileSize.TABLE_NAME + " (" +
                    TileSize._ID + " INTEGER PRIMARY KEY," +
                    TileSize.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    TileSize.COLUMN_SIZE_NAME + TEXT_TYPE + COMMA_SEP +
                    TileSize.COLUMN_Size_Value + TEXT_TYPE + COMMA_SEP + " )";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public InformationWallContract() {}

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }
    /* Inner class that defines the table contents for the Setting */
    public static abstract class Settings implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME_ENTRY_ID = "settingsID";
        public static final String COLUMN_TILE_SIZE_ID= "tileSizeID";
        public static final String COLUMN_IS_ACTIVATED = "isActivated";
        public static final String COLUMN_DRAWABLE_ID = "drawableID";
        public static final String COLUMN_SCREEN_NAME = "screenName";
    }
    /* Inner class that defines the table contents for the Tiles */
    public static abstract class Tiles implements BaseColumns {
        public static final String TABLE_NAME = "tiles";
        public static final String COLUMN_NAME_ENTRY_ID = "tilesID";
        public static final String COLUMN_TILE_NAME = "tileName";
        public static final String COLUMN_SETTINGS_ID = "settingsID";
    }
    /* Inner class that defines the table contents for the Tiles */
    public static abstract class TileSize implements BaseColumns {
        public static final String TABLE_NAME = "tileSize";
        public static final String COLUMN_NAME_ENTRY_ID = "tileSizeID";
        public static final String COLUMN_SIZE_NAME = "tileSizeName";
        public static final String COLUMN_Size_Value = "tileSizeValue";
    }
}
