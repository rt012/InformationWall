package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Eric Schmidt on 21.12.2015.
 */
public class FeedReader extends BaseActivity {
    private FloatingActionButton fab = null;
    private View mRootView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_reader);

        initViews();
        showFab(true);
        handleIntentData();

        openFragment(new FeedReaderOverview(), false);
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

    private void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootView = (CoordinatorLayout) findViewById(R.id.cl_feed_reader_layout);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FeedReaderAddFeed(), true);
            }
        });
    }

    private void handleIntentData(){
        //Tile Information
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(TileOverview.TILE_ID_KEY_PARAM)) {
            Tile currentTile = (Tile) DAOHelper.getTileDAO().queryForId(
                    getIntent().getExtras().getLong(TileOverview.TILE_ID_KEY_PARAM));
            if (currentTile.getIsActivated()) {
                Log.i("Feed Reader", "Show Feed Reader on Information Wall");
            }else{
                Log.i("Feed Reader", "Hide Feed Reader on Information Wall");
            }
        }
    }

    public void showFab(boolean showFab){
        //By User
        if (InfoWallApplication.getCurrentUser().getUserGroup().canWrite()) {
            if(showFab) {
                fab.show();
            }else{
                fab.hide();
            }
        }else{
            fab.hide();
        }
    }

    public void openFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.feed_reader_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        if (addToBackStack)fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
