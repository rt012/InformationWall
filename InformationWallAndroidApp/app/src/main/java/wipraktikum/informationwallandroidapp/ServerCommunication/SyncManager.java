package wipraktikum.informationwallandroidapp.ServerCommunication;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
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
            }
        });
        jsonManager.getJsonArray(ServerURLManager.GET_ALL_ITEMS_URL);
    }

    private void compareBlackBoardItems(JsonElement response) {
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<BlackBoardItem> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<BlackBoardItem>>(){}.getType());
        List<BlackBoardItem> clientItemList = blackBoardItemDAO.queryForAll();
        for(BlackBoardItem serverBlackBoardItem : serverItemList) {
            if(clientItemList.contains(serverBlackBoardItem)) {
                //BlackBoardItem ID
                serverBlackBoardItem.setBlackBoardItemID(clientItemList.
                        get(clientItemList.indexOf(serverBlackBoardItem)).getBlackBoardItemID());
                //Contact ID
                serverBlackBoardItem.getContact().setContactID(clientItemList.
                        get(clientItemList.indexOf(serverBlackBoardItem)).getContact().getContactID());
                //ContactAddress
                serverBlackBoardItem.getContact().getContactAddress().setContactAddressID(clientItemList.
                        get(clientItemList.indexOf(serverBlackBoardItem)).getContact().getContactAddress().getContactAddressID());
                //Attachment ID
                for (BlackBoardAttachment blackBoardAttachment : serverBlackBoardItem.getBlackBoardAttachment()) {
                    List<BlackBoardAttachment> clientBlackBoardAttachments = clientItemList.
                            get(clientItemList.indexOf(serverBlackBoardItem)).getBlackBoardAttachment();

                    if(clientBlackBoardAttachments.contains(blackBoardAttachment)){
                        blackBoardAttachment.setBlackBoardAttachmentID(clientBlackBoardAttachments.
                                get(clientBlackBoardAttachments.indexOf(blackBoardAttachment)).getBlackBoardAttachmentID());
                    }else{
                        blackBoardAttachment.setBlackBoardAttachmentID(0);
                    }
                }
                blackBoardItemDAO.update(serverBlackBoardItem);
            } else {
                serverBlackBoardItem.setBlackBoardItemID(0);
                serverBlackBoardItem.getContact().setContactID(0);
                serverBlackBoardItem.getContact().getContactAddress().setContactAddressID(0);

                for (BlackBoardAttachment blackBoardAttachment : serverBlackBoardItem.getBlackBoardAttachment()){
                    blackBoardAttachment.setBlackBoardAttachmentID(0);
                }
                blackBoardItemDAO.create(serverBlackBoardItem);
            }
        }
    }
}
