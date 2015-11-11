package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Hashtable;
import java.util.Map;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.PhpRequestManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoard extends BaseActivity {
    private OnActivityResultListener mOnActivityResultListener = null;
    private static Fragment currentFragment = null;
    private FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlackBoardAddItem blackBoardAddItem = BlackBoardAddItem.getInstance();
                blackBoardAddItem.setOnSaveBlackBoardItem(new BlackBoardAddItem.OnSaveBlackBoardItemListener() {
                    @Override
                    public void onSaveBlackBoardItem() {
                        onSupportNavigateUp();
                    }
                });
                openFragment(blackBoardAddItem, true);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if (getIntent().getExtras() != null){
            Tile currentTile = (Tile) DAOHelper.getInstance().getTileDAO().queryForId(
                    getIntent().getExtras().getLong(TileOverview.TILE_ID_KEY_PARAM));
            if (currentTile.getIsActivated()) {
                openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_OPEN);
            }else{
                openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_CLOSE);
            }
        }

        if (currentFragment == null || currentFragment == BlackBoardOverview.getInstance()) {
            openFragment(BlackBoardOverview.getInstance(), false);
        }

        //Show Fab
        showFabByFragment(currentFragment);
        //Change Title of Actionbar
        setTitle(getActionBarTitleByFragment(currentFragment));
    }

    @Override
    public void onPause() {
        super.onPause();
        openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_CLOSE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            //Get current Fragment
            Fragment fragment = getActiveFragment();
            currentFragment = fragment;
            //Change Title of Actionbar
            setTitle(getActionBarTitleByFragment(fragment));
            //Show Fab
            showFabByFragment(fragment);

            return true;
        }else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            //Get current Fragment
            Fragment fragment = getActiveFragment();
            currentFragment = fragment;
            //Change Title of Actionbar
            setTitle(getActionBarTitleByFragment(fragment));
            //Show Fab
            showFabByFragment(fragment);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FileHelper.PICK_ATTACHMENT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if(mOnActivityResultListener != null){
                mOnActivityResultListener.onActivityResult(data);
            }
        }
    }

    public void openBlackBoardOnServer(String actionParam){
        Map<String,String> params = new Hashtable<String, String>();
        params.put(ServerURLManager.OPEN_BLACK_BOARD_PARAM_KEY, actionParam);
        PhpRequestManager.getInstance().phpRequest(ServerURLManager.OPEN_BLACK_BOARD_URL, params);
    }

    public void openFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.black_board_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        if (addToBackStack)fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //Change Title of Actionbar
        setTitle(getActionBarTitleByFragment(fragment));
        //Show Fab
        showFabByFragment(fragment);

        currentFragment = fragment;
    }

    private String getActionBarTitleByFragment(Fragment fragment){
        if (fragment != null) {
            if (fragment.getClass().getSimpleName().equals(BlackBoardAddItem.class.getSimpleName())){
                //Check if a item is edited or a new one is created
                if (fragment.getArguments() != null){
                    return getString(R.string.activity_black_board_edit_item_title);
                }else {
                    return getString(R.string.activity_black_board_add_item_title);
                }
            }else if(fragment.getClass().getSimpleName().equals(BlackBoardOverview.class.getSimpleName())){
                return getString(R.string.activity_black_board_title);
            }
        }
        return getString(R.string.activity_black_board_title);
    }

    private void showFabByFragment(Fragment fragment){
        //By User
        if (InfoWallApplication.getCurrentUser().getUserGroup().canWrite()) {
            //By Fragment
            if (fragment != null) {
                if (fragment.getClass().getSimpleName().equals(BlackBoardAddItem.class.getSimpleName())){
                    fab.hide();
                }else if(fragment.getClass().getSimpleName().equals(BlackBoardOverview.class.getSimpleName())){
                    fab.show();
                }
            } else {
                fab.show();
            }
        }else{
            fab.hide();
        }
    }

    private Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    //Listener for OnActivityResult Event
    public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener){
        mOnActivityResultListener = onActivityResultListener;
    }

    public interface OnActivityResultListener{
        void onActivityResult(Intent data);
    }

    public void ShowSnakeMessage() {

    }
}
