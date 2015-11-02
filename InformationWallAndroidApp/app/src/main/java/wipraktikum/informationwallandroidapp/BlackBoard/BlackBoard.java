package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoard extends AppCompatActivity {
    private final String FRAGMENT_TAG_OVERVIEW = "FRAGMENT_OVERVIEW";
    private final String FRAGMENT_TAG_ADD_ITEM = "FRAGMENT_ADD_ITEM";

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

        if (currentFragment == null || currentFragment == BlackBoardOverview.getInstance()) {
            openFragment(BlackBoardOverview.getInstance(), false);
        }
        //Show Fab
        showFabByFragment(currentFragment);
        //Change Title of Actionbar
        setTitle(getActionBarTitleByFragment(currentFragment));
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

    public void openFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.black_board_fragment_container, fragment, ((IFragmentTag)fragment).getCustomTag());
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
            switch (((IFragmentTag) fragment).getCustomTag()) {
                case FRAGMENT_TAG_OVERVIEW:
                    return getString(R.string.activity_black_board_title);
                case FRAGMENT_TAG_ADD_ITEM:
                    return getString(R.string.activity_black_board_add_item_title);
                default:
                    return null;
            }
        }
        return getString(R.string.activity_black_board_title);
    }

    private void showFabByFragment(Fragment fragment){
        if (fragment != null) {
            switch (((IFragmentTag) fragment).getCustomTag()) {
                case FRAGMENT_TAG_OVERVIEW:
                    fab.show();
                    break;
                case FRAGMENT_TAG_ADD_ITEM:
                    fab.hide();
                    break;
                default:
                    fab.show();
                    break;
            }
        }else{
            fab.show();
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

    private Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener){
        mOnActivityResultListener = onActivityResultListener;
    }

    public interface OnActivityResultListener{
        void onActivityResult(Intent data);
    }
}
