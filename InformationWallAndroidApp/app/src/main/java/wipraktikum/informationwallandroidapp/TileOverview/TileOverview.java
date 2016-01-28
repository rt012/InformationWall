package wipraktikum.informationwallandroidapp.TileOverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONObject;

import java.util.List;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Tile.DBTile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.ORMLiteHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation.SyncManager;
import wipraktikum.informationwallandroidapp.TileOverview.Adapter.GridViewTileOverviewAdapter;
import wipraktikum.informationwallandroidapp.TileOverview.Dialog.TileLongClickDialog;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;


public class TileOverview extends BaseActivity {
    public final static String TILE_ID_KEY_PARAM = "tileID";

    private ORMLiteHelper databaseHelper = null;
    private CoordinatorLayout mRootView = null;
    private GridViewTileOverviewAdapter tileOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_overview);
        mRootView = (CoordinatorLayout) findViewById(R.id.cl_tileoverview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Set Adapter for GridView
        GridView gridView = (GridView)findViewById(R.id.gridview);
        tileOverviewAdapter = new GridViewTileOverviewAdapter(this);
        gridView.setAdapter(tileOverviewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityByTile(tileOverviewAdapter.getItem(position));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (InfoWallApplication.getCurrentUser().getUserGroup().canActivate()) {
                    showOnLongClickDialogByTile(tileOverviewAdapter.getItem(position), view.findViewById(R.id.tileBorder));
                }
                return true;
            }
        });

        //Register Listeners
        InfoWallApplication.getInstance().setOnPushReceiveListener(new InfoWallApplication.OnPushReceiveListener() {
            @Override
            public void onPushReceive() {
                tileOverviewAdapter.notifyDataSetChanged();
            }
        });

        initNewBlackBoardItemNotificationListener();
    }

    @Override
    public void onResume(){
        super.onResume();

        //Sync App Data
        syncAppData();
    }

    private void syncAppData(){
        //Sync Blackboard Items, Contacts, Tiles and Feeds from server and send unsynced items to it
        SyncManager syncManager = new SyncManager();
        syncManager.syncAll();
        syncManager.setOnSyncFinishedListener(new SyncManager.OnSyncFinishedListener() {
            @Override
            public void onSyncFinished() {
                updateTilesOnServer();
            }
        });

    }

    private void updateTilesOnServer() {
        //Show Tile if activated. Hide if not
        List<Tile> tileList = DAOHelper.getTileDAO().queryForAll();
        for (Tile tile : tileList){
            if(tile.getIsActivated()){
                activateTileOnServer(ServerURLManager.SHOW_BLACK_BOARD_PARAM_ACTIVE, tile);
            }else if (!tile.getIsActivated()){
                activateTileOnServer(ServerURLManager.SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE, tile);
            }
        }
    }

    private void startActivityByTile(Tile tile){
        try {
            //Put the Tile ID for reference to the tile
            Intent intent = new Intent(this, Class.forName(tile.getScreen()));
            Bundle params = new Bundle();
            params.putLong(TILE_ID_KEY_PARAM, tile.getTileID());
            intent.putExtras(params);

            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showOnLongClickDialogByTile(final Tile tile, final View isActivatedView){
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("tileTitle", tile.getName());
        args.putBoolean("isActivated", tile.getIsActivated());
        args.putInt("radioButtonPos", tile.getTileSize().ordinal());

        // Open LongClickDialog
        TileLongClickDialog tileLongClickDialog = new TileLongClickDialog();
        tileLongClickDialog.setArguments(args);
        tileLongClickDialog.show(getSupportFragmentManager(), TileLongClickDialog.class.getSimpleName());

        // Listeners
        tileLongClickDialog.setOnSwitchChangeListener(new TileLongClickDialog.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean isChecked) {
                Log.i(TileLongClickDialog.class.getSimpleName(), "Send message to Webserver - Tile activated");
                if (isChecked) {
                    activateTile(true, isActivatedView, tile);
                } else {
                    activateTile(false, isActivatedView, tile);
                }

            }
        });
        tileLongClickDialog.setOnRadioButtonChangeListener(new TileLongClickDialog.OnRadioButtonChangeListener() {
            @Override
            public void onRadioButtonChanged(int radioButtonPos) {
                changeTileSizeOnServer(DBTile.EnumTileSize.values()[radioButtonPos], tile);
                tile.setTileSize(DBTile.EnumTileSize.values()[radioButtonPos]);
                DAOHelper.getTileDAO().update(tile);
            }
        });
    }

    private void activateTile(boolean isActivated, View isActivatedWrapper, Tile tile){
        if(isActivated) {
            //isActivatedWrapper.setVisibility(View.VISIBLE);
            tile.setIsActivated(true);

            //Show Tile on information wall
            activateTileOnServer(ServerURLManager.SHOW_BLACK_BOARD_PARAM_ACTIVE, tile);
        }else{
            //isActivatedWrapper.setVisibility(View.GONE);
            tile.setIsActivated(false);

            //Hide Tile on information wall
            activateTileOnServer(ServerURLManager.SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE, tile);
        }
        DAOHelper.getTileDAO().update(tile);
        tileOverviewAdapter.notifyDataSetChanged();
    }

    private void changeTileSizeOnServer(DBTile.EnumTileSize tileSize, Tile tile){
        String actionParam = tileSize.toString() + tile.getName();
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.CHANGE_TILE_SIZE_KEY, actionParam);
        new JsonManager().sendJson(ServerURLManager.CHANGE_TILE_PARAM_URL, jsonObject);
    }

    //Only Blackboard is currently working (--> No more Tiles at the moment)
    private void activateTileOnServer(String actionParam, Tile tile){
        actionParam += tile.getName();
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.SHOW_BLACK_BOARD_PARAM_KEY, actionParam);
        new JsonManager().sendJson(ServerURLManager.CHANGE_TILE_PARAM_URL, jsonObject);
    }

    private void initNewBlackBoardItemNotificationListener() {
        //Register Listener
        NotificationHelper.getInstance().setOnNotificationReceiveListener(new NotificationHelper.OnNotificationReceiveListener() {
            @Override
            public void onNotificationReceive() {
                NotificationHelper.showNewBlackBoardItemSnackbar(getRootView(), getString(R.string.new_blackboard_item_notification_title));

            }
        });
    }

    @Override
    public View getRootView(){
        return mRootView;
    }
}
