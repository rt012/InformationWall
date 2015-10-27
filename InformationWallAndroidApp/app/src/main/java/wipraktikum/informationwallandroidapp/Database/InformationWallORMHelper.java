package wipraktikum.informationwallandroidapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.ContactAddress;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile;

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
    private Dao<BlackBoardItem, Long> blackBoardItemDAO;
    private Dao<BlackBoardAttachment, Long> blackBoardAttachmentDAO;
    private Dao<Contact, Long> contactDAO;
    private Dao<ContactAddress, Long> contactAdressDAO;

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
            TableUtils.createTable(connectionSource, BlackBoardItem.class);
            TableUtils.createTable(connectionSource, BlackBoardAttachment.class);
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, ContactAddress.class);

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
            TableUtils.dropTable(connectionSource, BlackBoardItem.class, false);
            TableUtils.dropTable(connectionSource, BlackBoardAttachment.class, false);
            TableUtils.dropTable(connectionSource, Contact.class, false);
            TableUtils.dropTable(connectionSource, ContactAddress.class, false);
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

    /**
     * Returns an instance of the BlackBoardItem data access object
     * @return
     * @throws SQLException
     */
    public Dao<BlackBoardItem, Long> getBlackBoardItemDAO() throws SQLException {
        try {
            if (blackBoardItemDAO == null) {
                blackBoardItemDAO = getDao(BlackBoardItem.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return blackBoardItemDAO;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Dao<BlackBoardAttachment, Long> getBlackBoardAttachmentDAO() throws SQLException {
        try {
            if (blackBoardAttachmentDAO == null) {
                blackBoardAttachmentDAO = getDao(BlackBoardAttachment.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return blackBoardAttachmentDAO;
    }

    public Dao<Contact, Long> getContactDAO() throws SQLException {
        try {
            if (contactDAO == null) {
                contactDAO = getDao(Contact.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return contactDAO;
    }

    public Dao<ContactAddress, Long> getContactAdressDAO() throws SQLException {
        try {
            if (contactAdressDAO == null) {
                contactAdressDAO = getDao(ContactAddress.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return contactAdressDAO;
    }

}
