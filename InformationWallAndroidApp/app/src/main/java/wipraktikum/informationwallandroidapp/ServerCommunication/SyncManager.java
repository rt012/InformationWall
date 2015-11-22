package wipraktikum.informationwallandroidapp.ServerCommunication;

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

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;

/**
 * Created by Remi on 04.11.2015.
 */

public class SyncManager implements JsonManager.OnObjectResponseListener, JsonManager.OnArrayResponseListener {

    private JsonManager jsonManagerToServer;
    private JsonManager jsonManagerFromServer;
    private static SyncManager instance;
    private BlackBoardItem currentUnsyncedBlackBoardItem;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private SyncManager() {
        jsonManagerToServer = new JsonManager();
        jsonManagerToServer.setOnObjectResponseReceiveListener(this);
        jsonManagerToServer.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                syncBlackBoardItemsFromServer();
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

    public static SyncManager getInstance(){
        if (instance == null){
            instance = new SyncManager();
        }
        return instance;
    }

    public void syncBlackBoardItems() {
        syncBlackBoardItemToServer();
    }

    private void syncBlackBoardItemToServer() {
        try {
            ArrayList<BlackBoardItem> unsyncedItems = DAOHelper.getInstance().getBlackBoardItemDAO().getUnsyncedItems();
            if(!unsyncedItems.isEmpty()) {
                currentUnsyncedBlackBoardItem = unsyncedItems.get(0);
                jsonManagerToServer.sendJson(ServerURLManager.NEW_BLACK_BOARD_ITEM_URL, currentUnsyncedBlackBoardItem);
            } else {
                syncBlackBoardItemsFromServer();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void syncBlackBoardItemsFromServer() {
        jsonManagerFromServer.getJsonArray(ServerURLManager.GET_ALL_ITEMS_URL);
    }

    private void UpdateOrCreateBlackBoardItems(JsonElement response) {
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();

        deleteAllBlackboardItems();

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<BlackBoardItem> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<BlackBoardItem>>(){}.getType());
        serverItemList = keepTransientUserData(serverItemList);

        for(BlackBoardItem serverBlackBoardItem : serverItemList) {
            serverBlackBoardItem.setSyncStatus(true);
            blackBoardItemDAO.createOrUpdate(serverBlackBoardItem);
        }
    }

    private List<BlackBoardItem> keepTransientUserData(List<BlackBoardItem> serverItemList) {
        for (BlackBoardItem blackBoardItem : serverItemList){
            User serverUser = blackBoardItem.getUser();
            User clientUser = (User) DAOHelper.getInstance().getUserDAO()
                    .queryForId(serverUser.getUserID());
            if (clientUser != null) {
                serverUser.setKeepLogInData(clientUser.isKeepLogInData());
                serverUser.setPreviousLoggedIn(clientUser.isPreviousLoggedIn());
                serverUser.setLoggedIn(clientUser.isLoggedIn());
            }
        }

        return serverItemList;
    }

    public void deleteAllBlackboardItems(){
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();
        ArrayList<BlackBoardItem> blackBoardItems = blackBoardItemDAO.queryForAll();

        for (BlackBoardItem blackBoardItem : blackBoardItems){
            blackBoardItemDAO.delete(blackBoardItem);
        }

    }

    @Override
    public void OnResponse(JSONObject response) {
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        BlackBoardItem serverBlackBoardItem = gsonInstance.fromJson(new JsonParser().parse(response.toString()), BlackBoardItem.class);
        serverBlackBoardItem.setSyncStatus(true);
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();
        blackBoardItemDAO.deleteByID(currentUnsyncedBlackBoardItem.getBlackBoardItemID());
        blackBoardItemDAO.createOrUpdate(serverBlackBoardItem);

        syncBlackBoardItems();
    }

    @Override
    public void OnResponse(JSONArray response) {
        UpdateOrCreateBlackBoardItems(new JsonParser().parse(response.toString()));
        if (mOnSyncFinishedListener != null){
            mOnSyncFinishedListener.onSyncFinished();
        }
    }

    //Listener for OnActivityResult Event
    public void setOnSyncFinishedListener(OnSyncFinishedListener onSyncFinishedListener){
        mOnSyncFinishedListener = onSyncFinishedListener;
    }

    public interface OnSyncFinishedListener{
        void onSyncFinished();
    }
}
