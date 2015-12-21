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
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.TransientManager;
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
                jsonManagerToServer.sendJson(ServerURLManager.NEW_BLACKBOARD_ITEM_URL, JSONBuilder.createJSONFromObject(currentUnsyncedBlackBoardItem));
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

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<BlackBoardItem> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<BlackBoardItem>>(){}.getType());
        ArrayList<BlackBoardItem> editedBlackBoardList = new ArrayList<BlackBoardItem>();
        for(BlackBoardItem serverBlackBoardItem : serverItemList) {
            serverBlackBoardItem = keepTransientUserData(serverBlackBoardItem);
            serverBlackBoardItem.setSyncStatus(true);
            editedBlackBoardList.add(serverBlackBoardItem);
        }

        deleteAllBlackboardItems();

        for(BlackBoardItem blackBoardItem : editedBlackBoardList) {
            blackBoardItemDAO.createOrUpdate(blackBoardItem);
        }
    }

    private BlackBoardItem keepTransientUserData(BlackBoardItem serverBlackBoardItem) {
        serverBlackBoardItem.setUser(TransientManager.keepTransientUserData(serverBlackBoardItem.getUser()));
        serverBlackBoardItem.setBlackBoardAttachment(
                TransientManager.keepTransientAttachmentList(serverBlackBoardItem.getBlackBoardAttachment()));

        return serverBlackBoardItem;
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
        serverBlackBoardItem = keepTransientUserData(serverBlackBoardItem);
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
