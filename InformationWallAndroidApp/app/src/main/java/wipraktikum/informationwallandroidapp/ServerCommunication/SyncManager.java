package wipraktikum.informationwallandroidapp.ServerCommunication;

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

public class SyncManager implements JsonManager.OnObjectResponseListener, JsonManager.OnArrayResponseListener {

    private JsonManager jsonManager;
    private static SyncManager instance;
    private BlackBoardItem currentUnsyncedBlackBoardItem;
    private Gson gsonInstance;

    private SyncManager() {
        jsonManager = new JsonManager();
        jsonManager.setOnObjectResponseReceiveListener(this);
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
                String jsonString =  gsonInstance.toJson(currentUnsyncedBlackBoardItem);
                jsonManager.sendJson(ServerURLManager.NEW_BLACK_BOARD_ITEM_URL, jsonString);
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
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<BlackBoardItem> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<BlackBoardItem>>(){}.getType());
        List<BlackBoardItem> clientItemList = blackBoardItemDAO.queryForAll();
        for(BlackBoardItem serverBlackBoardItem : serverItemList) {
            if(clientItemList.contains(serverBlackBoardItem)) {
                blackBoardItemDAO.update(serverBlackBoardItem);
            } else {
                blackBoardItemDAO.create(serverBlackBoardItem);
            }
        }
    }

    @Override
    public void OnResponse(JSONObject response) {
        BlackBoardItem serverBlackBoardItem = new Gson().fromJson(new JsonParser().parse(response.toString()), BlackBoardItem.class);
        serverBlackBoardItem.setSyncStatus(true);
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();
        blackBoardItemDAO.deleteByID(currentUnsyncedBlackBoardItem.getBlackBoardItemID());
        blackBoardItemDAO.create(serverBlackBoardItem);

        syncBlackBoardItems();
    }

    @Override
    public void OnResponse(JSONArray response) {
        UpdateOrCreateBlackBoardItems(new JsonParser().parse(response.toString()));
    }
}
