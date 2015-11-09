package wipraktikum.informationwallandroidapp.TileOverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.HashMap;
import java.util.Map;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Tile.DBTile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.PhpRequestManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.TileOverview.Adapter.GridViewTileOverviewAdapter;
import wipraktikum.informationwallandroidapp.TileOverview.Dialog.TileLongClickDialog;


public class TileOverview extends BaseActivity {
    private InformationWallORMHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Adapter for GridView
        final GridViewTileOverviewAdapter tileOverviewAdapter = new GridViewTileOverviewAdapter(this);
        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewTileOverviewAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityByTile(tileOverviewAdapter.getItem(position));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showOnLongClickDialogByTile(tileOverviewAdapter.getItem(position), view.findViewById(R.id.tileBorder));
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startActivityByTile(Tile tile){
        try {
            startActivity(new Intent(this, Class.forName(tile.getScreen())));
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
                DAOHelper.getInstance().getTileDAO().update(tile);
            }
        });
        tileLongClickDialog.setOnRadioButtonChangeListener(new TileLongClickDialog.OnRadioButtonChangeListener() {
            @Override
            public void onRadioButtonChanged(int radioButtonPos) {
                Log.i(TileLongClickDialog.class.getSimpleName(), "Send message to Webserver - Tile changed Size");
                tile.setTileSize(DBTile.EnumTileSize.values()[radioButtonPos]);
                DAOHelper.getInstance().getTileDAO().update(tile);
            }
        });
    }

    private void activateTile(boolean isActivated, View isActivatedWrapper, Tile tile){
        if(isActivated) {
            isActivatedWrapper.setVisibility(View.VISIBLE);
            tile.setIsActivated(true);

            //Show Tile on information wall
            Map<String,String> params = new HashMap<>();
            params.put(ServerURLManager.SHOW_BLACK_BOARD_PARAM_KEY, ServerURLManager.SHOW_BLACK_BOARD_PARAM_ACTIVE);
            PhpRequestManager.getInstance().phpRequest(ServerURLManager.SHOW_BLACK_BOARD_URL, params);
        }else{
            isActivatedWrapper.setVisibility(View.GONE);
            tile.setIsActivated(false);

            //Hide Tile on information wall
            Map<String,String> params = new HashMap<>();
            params.put(ServerURLManager.SHOW_BLACK_BOARD_PARAM_KEY, ServerURLManager.SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE);
            PhpRequestManager.getInstance().phpRequest(ServerURLManager.SHOW_BLACK_BOARD_URL, params);
        }
    }
}
