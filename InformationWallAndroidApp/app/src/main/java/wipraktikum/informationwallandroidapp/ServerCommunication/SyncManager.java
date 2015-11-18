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
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;

/**
 * Created by Remi on 04.11.2015.
 */

public class SyncManager implements JsonManager.OnObjectResponseListener, JsonManager.OnArrayResponseListener, JsonManager.OnErrorListener {

    private JsonManager jsonManager;
    private static SyncManager instance;
    private BlackBoardItem currentUnsyncedBlackBoardItem;
    private Gson gsonInstance;

    private SyncManager() {
        jsonManager = new JsonManager();
        jsonManager.setOnObjectResponseReceiveListener(this);
        jsonManager.setOnErrorReceiveListener(this);
        jsonManager.setOnArrayResponseReceiveListener(this);
        gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
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
                jsonManager.sendJson(ServerURLManager.NEW_BLACK_BOARD_ITEM_URL, currentUnsyncedBlackBoardItem);
            } else {
                syncBlackBoardItemsFromServer();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void syncBlackBoardItemsFromServer() {
        jsonManager.getJsonArray(ServerURLManager.GET_ALL_ITEMS_URL);
    }

    private void UpdateOrCreateBlackBoardItems(JsonElement response) {
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();

        deleteAllBlackboardItems();

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<BlackBoardItem> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<BlackBoardItem>>(){}.getType());
        for(BlackBoardItem serverBlackBoardItem : serverItemList) {
            serverBlackBoardItem.setSyncStatus(true);
            blackBoardItemDAO.createOrUpdate(serverBlackBoardItem);
        }
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
    }


    @Override
    public void OnErrorResponse(VolleyError error) {
        syncBlackBoardItemsFromServer();
    }
}
