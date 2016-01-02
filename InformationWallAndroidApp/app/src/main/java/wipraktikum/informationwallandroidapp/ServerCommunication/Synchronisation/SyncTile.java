package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.DAO.Tile.TileDAO;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

/**
 * Created by Eric Schmidt on 02.01.2016.
 */
public class SyncTile implements JsonManager.OnArrayResponseListener {
    private JsonManager jsonManagerFromServer;
    private static SyncTile instance;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private SyncTile() {
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

    public static SyncTile getInstance(){
        if (instance == null){
            instance = new SyncTile();
        }
        return instance;
    }

    public void syncTiles() {
        syncTilesFromServer();
    }

    private void syncTilesFromServer() {
        jsonManagerFromServer.getJsonArray(ServerURLManager.GET_ALL_BLACKBOARD_ITEMS_URL);
    }

    private void UpdateOrCreateBlackBoardItems(JsonElement response) {
        TileDAO tileDAO = DAOHelper.getTileDAO();

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Tile> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<Tile>>(){}.getType());

        deleteAllTiles();

        for(Tile tile : serverItemList) {
            tileDAO.createOrUpdate(tile);
        }
    }

    public void deleteAllTiles(){
        TileDAO tileDAO = DAOHelper.getTileDAO();
        ArrayList<Tile> tiles = tileDAO.queryForAll();

        for (Tile tile : tiles){
            tileDAO.delete(tile);
        }

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
