package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Feedreader.Adapter.FeedReaderListAdapter;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Eric Schmidt on 21.12.2015.
 */
public class Feedreader extends BaseActivity {
    private FloatingActionButton fab = null;
    private View mRootView = null;
    private ListView rssList = null;
    private EditText rssSearch = null;

    RSSSearch rssSearchManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_reader);

        rssSearchManager = new RSSSearch();

        initViews();
        showFab();
        handleIntentData();
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
            }
        });

        rssList = (ListView) findViewById(R.id.feed_reader_rss_list);

        rssSearch = (EditText) findViewById(R.id.input_rss_search);
        rssSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3) {
                    rssSearchManager.searchRSSByName(s.toString());
                    rssSearchManager.setOnSearchResponseReceiveListener(new RSSSearch.OnSearchResponseReceiveListener() {
                        @Override
                        public void OnSearchResponse(ArrayList<Feed> response) {
                            fillRSSList(response);
                        }
                    });
                }else{
                    fillRSSList(new ArrayList<Feed>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void fillRSSList(ArrayList<Feed> results){
        rssList.setAdapter(new FeedReaderListAdapter(this, 0, results));
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

    public void showFab(){
        //By User
        if (InfoWallApplication.getCurrentUser().getUserGroup().canWrite()) {
            fab.show();
        }else{
            fab.hide();
        }
    }

}
