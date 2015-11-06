package wipraktikum.informationwallandroidapp.ServerCommunication;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;

/**
 * Created by Remi on 04.11.2015.
 */

public class SyncManager {

    private JsonManager jsonManager;
    private static SyncManager instance;

    private SyncManager() {
        jsonManager = JsonManager.getInstance();
    }

    public static SyncManager getInstance(){
        if (instance == null){
            instance = new SyncManager();
        }
        return instance;
    }

    public void syncBlackBoardItems() {

        Gson gsonInstance = new Gson();
/*

        ArrayList<BlackBoardItem> unsyncedItems = null;
        try {
            unsyncedItems = DAOHelper.getInstance().getBlackBoardItemDAO().getUnsyncedItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(unsyncedItems != null && !unsyncedItems.isEmpty()) {
            for(BlackBoardItem item : unsyncedItems) {
                String jsonString =  gsonInstance.toJson(item);
                jsonManager.sendJson(JsonManager.NEW_BLACK_BOARD_ITEM_URL, jsonString);
            }
        }
*/

        jsonManager.setOnArrayResponseReceiveListener(new JsonManager.OnArrayResponseListener() {
            @Override
            public void OnResponse(JSONArray response) {
                compareBlackBoardItems(new JsonParser().parse(response.toString()));
            }
        });
        jsonManager.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnResponse(VolleyError error) {
                // TODO
                 System.out.print("asdasd");
            }
        });
        jsonManager.getJson(JsonManager.GET_ALL_ITEMS_URL);
    }

    private void compareBlackBoardItems(JsonElement response) {
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();
        Gson gsonInstance = new Gson();
        List<BlackBoardItem> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<BlackBoardItem>>(){}.getType());
        List<BlackBoardItem> clientItemList = blackBoardItemDAO.queryForAll();
        for(BlackBoardItem serverBlackBoardItem : serverItemList) {
            if(clientItemList.contains(serverBlackBoardItem)) {
                serverBlackBoardItem.setBlackBoardItemID(clientItemList.
                        get(clientItemList.indexOf(serverBlackBoardItem)).getBlackBoardItemID());
                blackBoardItemDAO.update(serverBlackBoardItem);
            } else {
                serverBlackBoardItem.setBlackBoardItemID(0);
                blackBoardItemDAO.create(serverBlackBoardItem);
            }
        }
    }
}
