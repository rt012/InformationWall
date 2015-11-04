package wipraktikum.informationwallandroidapp.ServerCommunication;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

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

        jsonManager.setOnResponseReceiveListener(new JsonManager.OnResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                compareBlackBoardItems(new JsonParser().parse(response.toString()));
            }
        });
        jsonManager.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnResponse(VolleyError error) {
                // TODO
            }
        });
        jsonManager.sendJson(JsonManager.GET_ALL_ITEMS_URL, null);
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
