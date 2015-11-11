package wipraktikum.informationwallandroidapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoard;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Tile.DBTile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
import wipraktikum.informationwallandroidapp.Login.LoginActivity;
import wipraktikum.informationwallandroidapp.ServerCommunication.SyncManager;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;
import wipraktikum.informationwallandroidapp.Utils.ParseUtils;

/**
 * Created by Remi on 26.10.2015.
 */
public class InfoWallApplication extends Application {
    private static InfoWallApplication instance;
    private static User currentUser;
    private InformationWallORMHelper databaseHelper;

    public static final String TAG = InfoWallApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;

    private static Activity activeActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());

        // register with parse
        ParseUtils.registerParse(this);
        //GCMHelper.getInstance().registerToGCM("asda");
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

    public static Activity getActiveActivity() {
        if(activeActivity != null) {
            return activeActivity
        }
        return null;
    }

    public static User getCurrentUser(){
        if (currentUser == null){
            currentUser = DAOHelper.getInstance().getUserDAO().getCurrentUser();
        }
        return currentUser;
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
        //Sync Black Board Items from server and send unsynced items to it
        SyncManager.getInstance().syncBlackBoardItems();

        //Tiles
        Dao<DBTile, Long> tileDao =  databaseHelper.getTileDAO();
        tileDao.createIfNotExists(new DBTile("Black Board", R.drawable.slide_1, BlackBoard.class.getName()));
        tileDao.createIfNotExists(new DBTile("Example Tile 1", R.drawable.slide_2, TileOverview.class.getName()));
        tileDao.createIfNotExists(new DBTile("Example Tile 2", R.drawable.slide_3, TileOverview.class.getName()));
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

    private static final class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.e("","onActivityCreated:" + activity.getLocalClassName());
            activeActivity = activity;
        }

        public void onActivityDestroyed(Activity activity) {
            Log.e("", "onActivityDestroyed:" + activity.getLocalClassName());
        }

        public void onActivityPaused(Activity activity) {
            Log.e("","onActivityPaused:" + activity.getLocalClassName());
        }

        public void onActivityResumed(Activity activity) {
            Log.e("","onActivityResumed:" + activity.getLocalClassName());
        }

        public void onActivitySaveInstanceState(Activity activity,
                                                Bundle outState) {
            Log.e("","onActivitySaveInstanceState:" + activity.getLocalClassName());
        }

        public void onActivityStarted(Activity activity) {
            Log.e("","onActivityStarted:" + activity.getLocalClassName());
        }

        public void onActivityStopped(Activity activity) {
            Log.e("","onActivityStopped:" + activity.getLocalClassName());
        }
    }
}
