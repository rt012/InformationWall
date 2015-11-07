package wipraktikum.informationwallandroidapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Contact.DBContact;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Contact.DBContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Tile.DBTile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.User.DBUser;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.User.DBUserGroup;

/**
 * Created by Eric Schmidt on 22.10.2015.
 */
public class InformationWallORMHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 1;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<DBTile, Long> tileDAO;
    private Dao<DBBlackBoardItem, Long> blackBoardItemDAO;
    private Dao<DBBlackBoardAttachment, Long> blackBoardAttachmentDAO;
    private Dao<DBContact, Long> contactDAO;
    private Dao<DBContactAddress, Long> contactAddressDAO;
    private Dao<DBUser, Long> userDAO;
    private Dao<DBUserGroup, Long> userGroupDAO;

    public InformationWallORMHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the Todo database table
             */
            TableUtils.createTable(connectionSource, DBTile.class);
            TableUtils.createTable(connectionSource, DBBlackBoardItem.class);
            TableUtils.createTable(connectionSource, DBBlackBoardAttachment.class);
            TableUtils.createTable(connectionSource, DBContact.class);
            TableUtils.createTable(connectionSource, DBContactAddress.class);
            TableUtils.createTable(connectionSource, DBUser.class);
            TableUtils.createTable(connectionSource, DBUserGroup.class);

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
            TableUtils.dropTable(connectionSource, DBTile.class, false);
            TableUtils.dropTable(connectionSource, DBBlackBoardItem.class, false);
            TableUtils.dropTable(connectionSource, DBBlackBoardAttachment.class, false);
            TableUtils.dropTable(connectionSource, DBContact.class, false);
            TableUtils.dropTable(connectionSource, DBContactAddress.class, false);
            TableUtils.dropTable(connectionSource, DBUser.class, false);
            TableUtils.dropTable(connectionSource, DBUserGroup.class, false);
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
    public Dao<DBTile, Long> getTileDAO() throws SQLException {
        try {
            if (tileDAO == null) {
                tileDAO = getDao(DBTile.class);
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
    public Dao<DBBlackBoardItem, Long> getBlackBoardItemDAO() throws SQLException {
        try {
            if (blackBoardItemDAO == null) {
                blackBoardItemDAO = getDao(DBBlackBoardItem.class);
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
    public Dao<DBBlackBoardAttachment, Long> getBlackBoardAttachmentDAO() throws SQLException {
        try {
            if (blackBoardAttachmentDAO == null) {
                blackBoardAttachmentDAO = getDao(DBBlackBoardAttachment.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return blackBoardAttachmentDAO;
    }

    public Dao<DBContact, Long> getContactDAO() throws SQLException {
        try {
            if (contactDAO == null) {
                contactDAO = getDao(DBContact.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return contactDAO;
    }

    public Dao<DBContactAddress, Long> getContactAddressDAO() throws SQLException {
        try {
            if (contactAddressDAO == null) {
                contactAddressDAO = getDao(DBContactAddress.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return contactAddressDAO;
    }

    public Dao<DBUser, Long> getUserDAO() throws SQLException {
        try {
            if (userDAO == null) {
                userDAO = getDao(DBUser.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return userDAO;
    }

    public Dao<DBUserGroup, Long> getUserGroupDAO() throws SQLException {
        try {
            if (userGroupDAO == null) {
                userGroupDAO = getDao(DBUserGroup.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return userGroupDAO;
    }
}
