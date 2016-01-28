package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

import android.util.Log;

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
import wipraktikum.informationwallandroidapp.ServerCommunication.TransientManager;

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
        jsonManagerFromServer.getJsonArray(ServerURLManager.GET_ALL_TILES_URL);
    }

    private void UpdateOrCreateTile(JsonElement response) {
        TileDAO tileDAO = DAOHelper.getTileDAO();

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Tile> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<Tile>>() {
        }.getType());
        List<Tile> editedItemList = new ArrayList<>();

        for (int i = 0; i < serverItemList.size(); i++) {
            Tile tile = serverItemList.get(i);
            tile = TransientManager.keepTransientTileData(tile);
            try {
                String tileStatus = response.getAsJsonArray().get(i).getAsJsonObject().get("mIsActivated").getAsString();
                Log.e("Volley", tileStatus);
                if(tileStatus.equals("active")) {
                    tile.setIsActivated(true);
                }
            }catch (Exception e) {
                Log.e("Volley", e.toString());
            }
            editedItemList.add(tile);
        }

        deleteAllTiles();

        for(Tile tile : editedItemList) {
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
        UpdateOrCreateTile(new JsonParser().parse(response.toString()));
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
