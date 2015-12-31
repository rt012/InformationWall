package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardExpandableListViewAdapter;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation.SyncManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;

public class BlackBoardOverview extends Fragment implements BlackBoardExpandableListViewAdapter.OnItemChangeListener, JsonManager.OnObjectResponseListener, JsonManager.OnErrorListener{
    private final String FRAGMENT_TAG = "FRAGMENT_OVERVIEW";

    private BlackBoardExpandableListViewAdapter blackBoardExpandableListViewAdapter = null;
    private BlackBoardItem deletedBlackBoardItem;
    private JsonManager jsonManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blackboard_overview, viewGroup, false);

        initViews(view);

        //Register Listener
        NotificationHelper.getInstance().setOnNotificationReceiveListener(new NotificationHelper.OnNotificationReceiveListener() {
            @Override
            public void onNotificationReceive() {
                blackBoardExpandableListViewAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(jsonManager == null) {
            jsonManager = new JsonManager();
            jsonManager.setOnObjectResponseReceiveListener(this);
            jsonManager.setOnErrorReceiveListener(this);
        }

        setTitle();
        showFab();
        deleteTempAttachments();
    }

    private void setTitle(){
        getActivity().setTitle(getString(R.string.fragment_black_board_title));
    }

    private void showFab(){
        ((Blackboard)getActivity()).showFab(true);
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
                syncManager.syncAll();
                syncManager.setOnSyncFinishedListener(new SyncManager.OnSyncFinishedListener() {
                    @Override
                    public void onSyncFinished() {
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.ex_lv_black_board);
        blackBoardExpandableListViewAdapter = new BlackBoardExpandableListViewAdapter(getActivity());
        blackBoardExpandableListViewAdapter.setOnItemChangeListener(this);
        expandableListView.setAdapter(blackBoardExpandableListViewAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                // Collapse previous parent if expanded.
                if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
    }

    private void deleteTempAttachments() {
        SharedPreferences savedPrefs = PreferenceManager.getDefaultSharedPreferences(
                InfoWallApplication.getInstance());
        SharedPreferences.Editor editor = savedPrefs.edit();
        editor.remove(BlackBoardAddItem.SAVE_ATTACHMENT_SHARED_PREF_KEY);
        editor.remove(BlackBoardAddItem.FRAGMENT_VISIBLE_SHARED_PREF_KEY);
        editor.apply();
    }

    @Override
    public void onDelete(BlackBoardItem blackBoardItem) {
        deletedBlackBoardItem = blackBoardItem;

        jsonManager.sendJson(ServerURLManager.DELETE_BLACK_BOARD_ITEM_URL, JSONBuilder.createJSONFromObject(blackBoardItem));
    }

    @Override
    public void onEdit(BlackBoardItem blackBoardItem) {
        openBlackboardAddItemWithArguments(blackBoardItem);
    }

    private void openBlackboardAddItemWithArguments(BlackBoardItem blackBoardItem){
        //Open BlackBoardAddItem with arguments
        Bundle params = new Bundle();
        params.putLong(BlackBoardAddItem.BLACK_BOARD_ITEM_ID_TAG, blackBoardItem.getBlackBoardItemID());

        BlackBoardAddItem blackBoardAddItem = new BlackBoardAddItem();
        blackBoardAddItem.setArguments(params);

        ((Blackboard)getActivity()).openFragment(blackBoardAddItem, true);
    }

    @Override
    public void OnResponse(JSONObject response) {
        DAOHelper.getBlackBoardItemDAO().delete(deletedBlackBoardItem);
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnErrorResponse(VolleyError error) {
        System.out.print("asdasd");
        DAOHelper.getBlackBoardItemDAO().delete(deletedBlackBoardItem);
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
    }
}
