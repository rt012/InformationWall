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

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Remi on 04.11.2015.
 */

public class SyncBlackboardItem implements JsonManager.OnObjectResponseListener, JsonManager.OnArrayResponseListener {

    private JsonManager jsonManagerToServer;
    private JsonManager jsonManagerFromServer;
    private static SyncBlackboardItem instance;
    private BlackBoardItem currentUnsyncedBlackBoardItem;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private SyncBlackboardItem() {
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

    public static SyncBlackboardItem getInstance(){
        if (instance == null){
            instance = new SyncBlackboardItem();
        }
        return instance;
    }

    public void syncBlackBoardItems() {
        syncBlackBoardItemToServer();
    }

    private void syncBlackBoardItemToServer() {
        try {
            ArrayList<BlackBoardItem> unsyncedItems = DAOHelper.getBlackBoardItemDAO().getUnsyncedItems();
            if(!unsyncedItems.isEmpty()) {
                currentUnsyncedBlackBoardItem = unsyncedItems.get(0);

                JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.NEW_BLACKBOARD_ITEM_KEY,
                        JSONBuilder.createJSONFromObject(currentUnsyncedBlackBoardItem));
                jsonManagerToServer.sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
            } else {
                syncBlackBoardItemsFromServer();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void syncBlackBoardItemsFromServer() {
        jsonManagerFromServer.getJsonArray(ServerURLManager.GET_ALL_BLACKBOARD_ITEMS_URL);
    }

    private void UpdateOrCreateBlackBoardItems(JsonElement response) {
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getBlackBoardItemDAO();

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
            User clientUser = (User) DAOHelper.getUserDAO()
                    .queryForId(serverUser.getUserID());
            if (clientUser != null) {
                serverUser.setServerURL(clientUser.getServerURL());
                serverUser.setKeepLogInData(clientUser.isKeepLogInData());
                serverUser.setPreviousLoggedIn(clientUser.isPreviousLoggedIn());
                serverUser.setLoggedIn(clientUser.isLoggedIn());
            }
        }

        return serverItemList;
    }

    public void deleteAllBlackboardItems(){
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getBlackBoardItemDAO();
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
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getBlackBoardItemDAO();
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

    public void setOnSyncFinishedListener(OnSyncFinishedListener onSyncFinishedListener){
        mOnSyncFinishedListener = onSyncFinishedListener;
    }

    public interface OnSyncFinishedListener{
        void onSyncFinished();
    }
}
