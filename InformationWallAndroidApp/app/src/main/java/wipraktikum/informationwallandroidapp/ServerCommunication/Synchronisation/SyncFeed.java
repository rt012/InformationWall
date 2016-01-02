package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.DAO.FeedReader.FeedReaderDAO;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Eric Schmidt on 02.01.2016.
 */
public class SyncFeed implements JsonManager.OnObjectResponseListener, JsonManager.OnArrayResponseListener {
    private JsonManager jsonManagerToServer;
    private JsonManager jsonManagerFromServer;
    private static SyncFeed instance;
    private Feed currentUnsyncedFeed;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private SyncFeed() {
        jsonManagerToServer = new JsonManager();
        jsonManagerToServer.setOnObjectResponseReceiveListener(this);
        jsonManagerToServer.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                syncFeedFromServer();
            }
        });
        jsonManagerToServer.setOnArrayResponseReceiveListener(this);

        jsonManagerFromServer = new JsonManager();
        jsonManagerFromServer.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                if (mOnSyncFinishedListener != null){
                    mOnSyncFinishedListener.onSyncFinished();
                }
            }
        });
        jsonManagerFromServer.setOnArrayResponseReceiveListener(this);
    }

    public static SyncFeed getInstance(){
        if (instance == null){
            instance = new SyncFeed();
        }
        return instance;
    }

    public void syncFeeds() {
        syncFeedsToServer();
    }

    private void syncFeedsToServer() {
        try {
            ArrayList<Feed> unsyncedItems = DAOHelper.getFeedReaderDAO().getUnsyncedItems();
            if(!unsyncedItems.isEmpty()) {
                currentUnsyncedFeed = unsyncedItems.get(0);
                jsonManagerToServer.sendJson(ServerURLManager.NEW_FEED_URL, JSONBuilder.createJSONFromObject(currentUnsyncedFeed));
            } else {
                syncFeedFromServer();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void syncFeedFromServer() {
        jsonManagerFromServer.getJsonArray(ServerURLManager.GET_ALL_FEEDS_URL);
    }

    private void UpdateOrCreateBlackBoardItems(JsonElement response) {
        FeedReaderDAO feedReaderDAO = DAOHelper.getFeedReaderDAO();

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Feed> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<Feed>>(){}.getType());

        deleteAllFeeds();

        for(Feed feed : serverItemList) {
            feed.setSyncStatus(true);
            feedReaderDAO.createOrUpdate(feed);
        }
    }

    public void deleteAllFeeds(){
        FeedReaderDAO feedReaderDAO = DAOHelper.getFeedReaderDAO();
        ArrayList<Feed> feeds = feedReaderDAO.queryForAll();

        for (Feed feed : feeds){
            feedReaderDAO.delete(feed);
        }

    }

    @Override
    public void OnResponse(JSONObject response) {
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Feed serverFeed = gsonInstance.fromJson(new JsonParser().parse(response.toString()), Feed.class);
        serverFeed.setSyncStatus(true);
        FeedReaderDAO feedReaderDAO = DAOHelper.getFeedReaderDAO();
        feedReaderDAO.deleteByID(currentUnsyncedFeed.getFeedReaderID());
        feedReaderDAO.createOrUpdate(serverFeed);

        syncFeeds();
    }

    @Override
    public void OnResponse(JSONArray response) {
        UpdateOrCreateBlackBoardItems(new JsonParser().parse(response.toString()));
        if (mOnSyncFinishedListener != null){
            mOnSyncFinishedListener.onSyncFinished();
        }
    }

    public void setOnSyncFinishedListener(OnSyncFinishedListener onSyncFinishedListener){
        mOnSyncFinishedListener = onSyncFinishedListener;
    }

    public interface OnSyncFinishedListener{
        void onSyncFinished();
    }
}
