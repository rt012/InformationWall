package wipraktikum.informationwallandroidapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;

import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoard;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContact;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBTile;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
import wipraktikum.informationwallandroidapp.Login.LoginActivity;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Remi on 26.10.2015.
 */
public class InfoWallApplication extends Application {
    private static InfoWallApplication instance;
    private InformationWallORMHelper databaseHelper;

    public static final String TAG = InfoWallApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //Insert Database dummy date
        databaseHelper = InfoWallApplication.getInstance().getDatabaseHelper();

        try {
            insertTestData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        startActivity();
    }

    public static InfoWallApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public InformationWallORMHelper getDatabaseHelper() {
        if (databaseHelper == null) {
           databaseHelper = new InformationWallORMHelper(this);
        }
        return databaseHelper;
    }


    /**
     * Testing out the TodoOrmLiteExample app by creating some Todo entries in the database,
     * and querying for all the Todo object from the todo table.
     * @throws SQLException
     */
    private void insertTestData() throws SQLException {
        //SyncManager.getInstance().syncBlackBoardItems();
        //Tiles
        Dao<DBTile, Long> tileDao =  databaseHelper.getTileDAO();
        tileDao.createIfNotExists(new DBTile("Black Board", R.drawable.slide_1, BlackBoard.class.getName()));
        tileDao.createIfNotExists(new DBTile("Example Tile 1", R.drawable.slide_2, TileOverview.class.getName()));
        tileDao.createIfNotExists(new DBTile("Example Tile 2", R.drawable.slide_3, TileOverview.class.getName()));

        //Blackboard
        Dao<wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem, Long> blackBoardItemsDAO =databaseHelper.getBlackBoardItemDAO();
        DBContact dummyContact = new DBContact("Max", "Mustermann", "maxMustermann@test.de", "234242342345", "Wunschfirma XY", new DBContactAddress(0, "Teststraße", "23", "242342", "Stuttgart"));
        DBContact dummyContact2 = new DBContact("Matthilda", "Musterfrau", "matthildaMusterfrau@test.de", "234242342345", "Wunschfirma XY", new DBContactAddress(0, "Teststraße", "23", "242342", "Stuttgart"));

        wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem item = new wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem("Stellenanzeige", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", new Date(), new Date());
        blackBoardItemsDAO.createIfNotExists(item);
        DBBlackBoardAttachment ba1 = new DBBlackBoardAttachment("http://www.iso.org/iso/annual_report_2009.pdf", null, DBBlackBoardAttachment.DataType.PDF,item);
        DBBlackBoardAttachment ba2 = new DBBlackBoardAttachment("http://cppstudio.com/wp-content/images/article/test.jpg", null, DBBlackBoardAttachment.DataType.IMG,item);
        DBBlackBoardAttachment ba3 = new DBBlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test1.pdf", DBBlackBoardAttachment.DataType.PDF,item);

        databaseHelper.getBlackBoardAttachmentDAO().createIfNotExists(ba1);
        databaseHelper.getBlackBoardAttachmentDAO().createIfNotExists(ba2);
        databaseHelper.getBlackBoardAttachmentDAO().createIfNotExists(ba3);

        wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem item2 = new wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem("Praktikum", dummyContact2, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", new Date(), new Date());
        blackBoardItemsDAO.createIfNotExists(item2);

        DBBlackBoardAttachment ba4 = new DBBlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test1.pdf", DBBlackBoardAttachment.DataType.PDF,item2);
        DBBlackBoardAttachment ba5 = new DBBlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test1.pdf", DBBlackBoardAttachment.DataType.PDF,item2);
        databaseHelper.getBlackBoardAttachmentDAO().createIfNotExists(ba4);
        databaseHelper.getBlackBoardAttachmentDAO().createIfNotExists(ba5);

    }

    private void startActivity() {
        if(checkIfUserIsLoggedIn()){
            // Start Tile Overview
            Intent intent = new Intent(this, TileOverview.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Start Login Screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private boolean checkIfUserIsLoggedIn() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getBoolean("loggedIn", false);
    }
}
