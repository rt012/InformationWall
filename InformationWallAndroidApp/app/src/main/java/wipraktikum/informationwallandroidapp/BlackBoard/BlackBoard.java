package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoard extends BaseActivity{
    private OnActivityResultListener mOnActivityResultListener = null;
    private Fragment currentFragment = null;
    private FloatingActionButton fab = null;
    private View mRootView = null;
    private boolean isFilePickerVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_board);
        mRootView = (CoordinatorLayout) findViewById(R.id.cl_blackboard_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlackBoardAddItem blackBoardAddItem = new BlackBoardAddItem();
                openFragment(blackBoardAddItem, true);
            }
        });
    }

    @Override
    public View getRootView(){
        return mRootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        if (getIntent().getExtras() != null){
            Tile currentTile = (Tile) DAOHelper.getTileDAO().queryForId(
                    getIntent().getExtras().getLong(TileOverview.TILE_ID_KEY_PARAM));
            if (currentTile.getIsActivated()) {
                openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_OPEN);
            }else{
                openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_CLOSE);
            }
        }

        if (currentFragment == null) {
            openFragment(new BlackBoardOverview(), false);
        } else {
            openFragment(currentFragment, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isFilePickerVisible) {
            openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_CLOSE);
        }else{
            isFilePickerVisible = false;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            //Get current Fragment
            Fragment fragment = getActiveFragment();
            if(fragment == null) {
                fragment = new BlackBoardOverview();
            }
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
            if(fragment == null) {
                fragment = new BlackBoardOverview();
            }
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

    public void openBlackBoardOnServer(String actionParam) {
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.OPEN_BLACK_BOARD_PARAM_KEY, actionParam);
        new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
    }

    public void openFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.black_board_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        if (addToBackStack)fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        //Change Title of Actionbar
        setTitle(getActionBarTitleByFragment(fragment));
        //Show Fab
        showFabByFragment(fragment);

        if (fragment instanceof BlackBoardAddItem){
            ((BlackBoardAddItem) fragment).setOnSaveBlackBoardItem(new BlackBoardAddItem.OnSaveBlackBoardItemListener() {
                @Override
                public void onSaveBlackBoardItem(boolean isSuccessful) {
                    if (!isSuccessful) {
                        Snackbar.make(getRootView(), R.string.black_board_add_item_snackbar_connection_error, Snackbar.LENGTH_LONG).show();
                    }
                    onSupportNavigateUp();
                }
            });
        }
        ;

        currentFragment = fragment;
    }

    private String getActionBarTitleByFragment(Fragment fragment){
        if (fragment != null) {
            if (fragment.getClass().getSimpleName().equals(BlackBoardAddItem.class.getSimpleName())){
                //Check if a item is edited or a new one is created
                if (fragment.getArguments() != null){
                    return getString(R.string.fragment_black_board_edit_item_title);
                }else {
                    return getString(R.string.fragment_black_board_add_item_title);
                }
            }else if(fragment.getClass().getSimpleName().equals(BlackBoardOverview.class.getSimpleName())){
                return getString(R.string.fragment_black_board_title);
            }
        }
        return getString(R.string.fragment_black_board_title);
    }

    private void showFabByFragment(Fragment fragment){
        //By User
        if (InfoWallApplication.getCurrentUser().getUserGroup().canWrite()) {
            //By Fragment
            if (fragment != null) {
                if (fragment.getClass().getSimpleName().equals(BlackBoardAddItem.class.getSimpleName())){
                    fab.hide();
                    //Add Listener
                    ((BlackBoardAddItem)fragment).setOnStartActivityResultListener(new BlackBoardAddItem.OnStartActivityResultListener() {
                        @Override
                        public void onStartActivityResultListener() {
                            isFilePickerVisible = true;
                        }
                    });

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
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getClass().getSimpleName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    //Listener for OnActivityResult Event
    public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener){
        mOnActivityResultListener = onActivityResultListener;
    }

    public interface OnActivityResultListener{
        void onActivityResult(Intent data);
    }
}
