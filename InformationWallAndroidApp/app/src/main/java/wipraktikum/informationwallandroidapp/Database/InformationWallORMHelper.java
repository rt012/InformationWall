package wipraktikum.informationwallandroidapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.Tile;

/**
 * Created by Eric Schmidt on 22.10.2015.
 */
public class InformationWallORMHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 1;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<Tile, Long> tileDAO;

    public InformationWallORMHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the Todo database table
             */
            TableUtils.createTable(connectionSource, Tile.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            TableUtils.dropTable(connectionSource, Tile.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of the Tile data access object
     * @return
     * @throws SQLException
     */
    public Dao<Tile, Long> getTileDAO() throws SQLException {
        try {
            if (tileDAO == null) {
                tileDAO = getDao(Tile.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return tileDAO;
    }

}
