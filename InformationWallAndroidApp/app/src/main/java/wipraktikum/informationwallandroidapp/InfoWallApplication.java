package wipraktikum.informationwallandroidapp;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoard;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.ContactAddress;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
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
        //Tiles
        Dao<Tile, Long> tileDao =  databaseHelper.getTileDAO();
        tileDao.createIfNotExists(new Tile("Example Tile 1", R.drawable.slide_1, BlackBoard.class.getName()));
        tileDao.createIfNotExists(new Tile("Example Tile 2", R.drawable.slide_2, TileOverview.class.getName()));
        tileDao.createIfNotExists(new Tile("Example Tile 3", R.drawable.slide_3, TileOverview.class.getName()));

        //Blackboard
        Dao<BlackBoardItem, Long> blackBoardItemsDAO =databaseHelper.getBlackBoardItemDAO();
        List<BlackBoardAttachment> attachmentList = new ArrayList<BlackBoardAttachment>();
        attachmentList.add(new BlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test1.pdf", BlackBoardAttachment.DataType.PDF));
        attachmentList.add(new BlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test2.pdf", BlackBoardAttachment.DataType.PDF));
        attachmentList.add(new BlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test3.pdf", BlackBoardAttachment.DataType.PDF));
        attachmentList.add(new BlackBoardAttachment("http://localhost/imagestore/test.pdf", "C://temp/test/test4.pdf", BlackBoardAttachment.DataType.IMG));

        Contact dummyContact = new Contact("Max", "Mustermann", "maxMustermann@test.de", "234242342345", "Wunschfirma XY", new ContactAddress(0, "Teststraße", "23", "242342", "Stuttgart"));

        BlackBoardItem item = new BlackBoardItem("Stellenanzeige", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", new Date(), new Date());
        blackBoardItemsDAO.createIfNotExists(item);
        blackBoardItemsDAO.queryForId(item.getBlackBoardItemID()).setBlackBoardAttachment(attachmentList);

        item = new BlackBoardItem("Praktikum", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", new Date(), new Date());
        blackBoardItemsDAO.createIfNotExists(item);
        blackBoardItemsDAO.queryForId(item.getBlackBoardItemID()).setBlackBoardAttachment(attachmentList);


        item = new BlackBoardItem("Wohnungssuche", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", new Date(), new Date());
        blackBoardItemsDAO.createIfNotExists(item);
        blackBoardItemsDAO.queryForId(item.getBlackBoardItemID()).setBlackBoardAttachment(attachmentList);

        item = new BlackBoardItem("Handy zu verkaufen", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", new Date(), new Date());
        blackBoardItemsDAO.createIfNotExists(item);
        blackBoardItemsDAO.queryForId(item.getBlackBoardItemID()).setBlackBoardAttachment(attachmentList);

    }
}
