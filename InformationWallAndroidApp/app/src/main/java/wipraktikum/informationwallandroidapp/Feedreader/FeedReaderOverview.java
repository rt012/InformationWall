package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Feedreader.Adapter.FeedReaderListAdapter;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation.SyncManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
public class FeedReaderOverview extends Fragment {
    private ListView rssList = null;

    private FeedReaderListAdapter adapter = null;
    private List feedList = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_reader_overview, viewGroup, false);
        setHasOptionsMenu(true);

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        initViews(getView());
        ((FeedReader) getActivity()).showFab(true);
    }

    private void initViews(View view){
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncManager syncManager = new SyncManager();
                syncManager.syncFeedReaderInformation();
                syncManager.setOnSyncFinishedListener(new SyncManager.OnSyncFinishedListener() {
                    @Override
                    public void onSyncFinished() {
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                        fillRSSList();
                    }
                });
            }
        });

        rssList = (ListView) view.findViewById(R.id.feed_reader_rss_list);
        rssList.setEmptyView(view.findViewById(R.id.empty));
        fillRSSList();
    }

    private void fillRSSList(){
        feedList = DAOHelper.getFeedReaderDAO().queryForAll();
        adapter = new FeedReaderListAdapter(getActivity(), 0, feedList);
        rssList.setAdapter(adapter);
        adapter.setOnDeleteFeedListener(new FeedReaderListAdapter.OnDeleteFeedListener() {
            @Override
            public void onDeleteFeed(Feed feed) {
                adapter.remove(feed);
                DAOHelper.getFeedReaderDAO().delete(feed);
                if (feed.isSyncStatus()) {
                    new JsonManager().sendJson(ServerURLManager.DELETE_FEED_URL, JSONBuilder.createJSONFromObject(feed));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
