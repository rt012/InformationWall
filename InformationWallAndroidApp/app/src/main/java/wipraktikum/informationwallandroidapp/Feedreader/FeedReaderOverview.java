package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Feedreader.Adapter.FeedReaderListAdapter;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation.SyncManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;
import wipraktikum.informationwallandroidapp.Utils.ProgressDialogHelper;
import wipraktikum.informationwallandroidapp.Utils.UndoDeleteHelper;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
public class FeedReaderOverview extends Fragment {
    private ListView rssList = null;

    private FeedReaderListAdapter adapter = null;
    private List feedList = null;
    private UndoDeleteHelper undoDeleteHelper = null;
    private ProgressDialogHelper progressDialog = null;

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
        checkUserRights();
        ((FeedReader) getActivity()).showFab(true);

        undoDeleteHelper = new UndoDeleteHelper(((FeedReader)getActivity()).getRootView(), getActivity());
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

                undoDeleteHelper.showUndoSnackbar(feed);
                undoDeleteHelper.setOnUndoDeleteListener(new UndoDeleteHelper.OnUndoDeleteListener() {
                    @Override
                    public void onUndo(ArrayList<Object> undoObjects) {
                        for (Object object : undoObjects) {
                            Feed undoFeed = (Feed) object;
                            restoreFeedInDB(undoFeed);
                            restoreFeedOnServer(undoFeed);
                            fillRSSList();
                        }
                    }
                });
            }
        });
    }

    private void checkUserRights(){
        User currentUser = InfoWallApplication.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getUserGroup().canDelete()){
                adapter.setEnableSwipe(true);
            }else{
                adapter.setEnableSwipe(false);
            }
        }
    }

    private void restoreFeedInDB(Feed feed){
        DAOHelper.getFeedReaderDAO().create(feed);
    }

    private void restoreFeedOnServer(final Feed feed){
        showProgressDialog();

        JsonManager jsonManager = new JsonManager();
        JSONObject feedJson = JSONBuilder.createJSONFromObject(feed);
        jsonManager.sendJson(ServerURLManager.NEW_FEED_URL, feedJson);
        jsonManager.setOnObjectResponseReceiveListener(new JsonManager.OnObjectResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                updateFeedFromServer(response, feed);
                hideProgressDialog();
            }
        });
        jsonManager.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                ((FeedReader) getActivity()).showSnackBar(R.string.black_board_add_item_snackbar_connection_error);
                hideProgressDialog();
            }
        });
    }

    private void updateFeedFromServer(JSONObject response, Feed feed) {
        Feed serverFeed = Feed.parseItemFromJson(response.toString());
        serverFeed.setSyncStatus(true);
        DAOHelper.getFeedReaderDAO().delete(feed);
        DAOHelper.getFeedReaderDAO().createOrUpdate(serverFeed);
        fillRSSList();
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialogHelper(getActivity());
        }

        progressDialog.show(getString(R.string.progress_pleaseWait), getString(R.string.progress_itemUpload));
    }

    private void hideProgressDialog(){
       progressDialog.hide();
    }


}
