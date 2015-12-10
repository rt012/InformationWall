package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardUtils.BlackBoardAnimationUtils;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;
import wipraktikum.informationwallandroidapp.Utils.ActivityHelper;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.RealPathHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoard extends BaseActivity{
    private OnActivityResultListener mOnActivityResultListener = null;
    private OnLayoutSelectionListener mOnLayoutSelectionListener = null;

    private FloatingActionButton fab = null;
    private View mRootView = null;
    private boolean isFilePickerVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackboard);
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

        openFragment(new BlackBoardOverview(), false);
        handleIntentData();
    }

    @Override
    public View getRootView(){
        return mRootView;
    }

    private void handleIntentData(){
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if(InfoWallApplication.getCurrentUser() == null) {
            ActivityHelper.openLoginActivity(this);
            return;
        }else if(InfoWallApplication.getCurrentUser().getUserGroup().canWrite()) {
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                prepareOpenBlackboardAddItemWithArguments(createAttachmentUriFromIntent(false));
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                prepareOpenBlackboardAddItemWithArguments(createAttachmentUriFromIntent(true));
            }
        } else {
            ActivityHelper.openBlackboardActivity(this);
            return;
        }


        //Tile Information
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(TileOverview.TILE_ID_KEY_PARAM)) {
            Tile currentTile = (Tile) DAOHelper.getTileDAO().queryForId(
                    getIntent().getExtras().getLong(TileOverview.TILE_ID_KEY_PARAM));
            if (currentTile.getIsActivated()) {
                openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_OPEN);
            }else{
                openBlackBoardOnServer(ServerURLManager.OPEN_BLACK_BOARD_PARAM_CLOSE);
            }
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

            return true;
        }else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
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

    private List<Uri> createAttachmentUriFromIntent(boolean isMultiple){
        List<Uri> attachmentUris = new ArrayList<>();

        if (isMultiple){
            attachmentUris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        }else{
            Uri imageUri = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            attachmentUris.add(imageUri);
        }

        return attachmentUris;
    }

    private void prepareOpenBlackboardAddItemWithArguments(List<Uri> attachmentUris){
        //Open BlackBoardAddItem with arguments
        Bundle params = new Bundle();
        ArrayList<String> attachmentPathList = new ArrayList<>();
        for (Uri uri : attachmentUris){
            String attachmentPath  = RealPathHelper.getInstance().getRealPathFromURI(uri);
            attachmentPathList.add(attachmentPath);
        }
        params.putStringArrayList(BlackBoardAddItem.ATTACHMENT_Path_LIST_TAG, attachmentPathList);

        BlackBoardAddItem blackboardAddItem = new BlackBoardAddItem();
        blackboardAddItem.setArguments(params);

        openFragment(blackboardAddItem, true);
    }

    public void openBlackBoardOnServer(String actionParam) {
        BlackBoardAnimationUtils.openBlackBoardOnServer(actionParam);
    }

    public void openFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.black_board_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        if (addToBackStack)fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        //Set Listener
        setFragmentListener(fragment);
    }

    private void setFragmentListener(Fragment fragment){
        if (fragment instanceof BlackBoardAddItem){
            //Add Listener
            ((BlackBoardAddItem)fragment).setOnStartActivityResultListener(new BlackBoardAddItem.OnStartActivityResultListener() {
                @Override
                public void onStartActivityResultListener() {
                    isFilePickerVisible = true;
                }
            });
            ((BlackBoardAddItem) fragment).setOnSaveBlackBoardItemListener(new BlackBoardAddItem.OnSaveBlackBoardItemListener() {
                @Override
                public void onSaveBlackBoardItem(boolean isSuccessful) {
                    if (!isSuccessful) {
                        Snackbar.make(getRootView(), R.string.black_board_add_item_snackbar_connection_error, Snackbar.LENGTH_LONG).show();
                    }
                    onSupportNavigateUp();
                }
            });
        }else if(fragment instanceof BlackBoardItemLayoutSelection){
            ((BlackBoardItemLayoutSelection)fragment).setOnLayoutSelectListener(new BlackBoardItemLayoutSelection.OnLayoutSelectListener() {
                @Override
                public void onLayoutSelect(DBBlackBoardItem.LayoutType layoutType) {
                    if (mOnLayoutSelectionListener != null) {
                        mOnLayoutSelectionListener.OnLayoutSelect(layoutType);
                    }
                    onSupportNavigateUp();
                }
            });
        }
    }

    public void showFab(boolean isVisible){
        //By User
        if (InfoWallApplication.getCurrentUser().getUserGroup().canWrite()) {
            if (isVisible) {
                fab.show();
            } else {
                fab.hide();
            }
        }else{
            fab.hide();
        }
    }

    //Listener for OnActivityResult Event
    public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener){
        mOnActivityResultListener = onActivityResultListener;
    }

    public interface OnActivityResultListener{
        void onActivityResult(Intent data);
    }
    //Listener for OnLayoutChange Event in BlackBoardLayoutSelection
    public void setOnLayoutSelectionListener(OnLayoutSelectionListener onLayoutSelectionListener){
        mOnLayoutSelectionListener = onLayoutSelectionListener;
    }

    public interface OnLayoutSelectionListener{
        void OnLayoutSelect(DBBlackBoardItem.LayoutType layoutType);
    }
}
