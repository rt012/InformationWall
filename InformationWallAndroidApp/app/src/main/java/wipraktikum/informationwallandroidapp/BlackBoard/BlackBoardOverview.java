package wipraktikum.informationwallandroidapp.BlackBoard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardExpandableListViewAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.Dialog.BlackBoardItemDialogBuilder;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.SyncManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

public class BlackBoardOverview extends Fragment implements BlackBoardItemDialogBuilder.OnItemChangeListener, JsonManager.OnObjectResponseListener, JsonManager.OnErrorListener{
    private final String FRAGMENT_TAG = "FRAGMENT_OVERVIEW";

    private BlackBoardExpandableListViewAdapter blackBoardExpandableListViewAdapter = null;
    private BlackBoardItemDialogBuilder blackBoardItemDialogBuilder = null;
    private BlackBoardItem deletedBlackBoardItem;
    private JsonManager jsonManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_board_overview, viewGroup, false);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncManager.getInstance().syncBlackBoardItems();
                SyncManager.getInstance().setOnSyncFinishedListener(new SyncManager.OnSyncFinishedListener() {
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
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            View previousView = null;
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (previousView != null) {
                    previousView.setBackgroundColor(Color.TRANSPARENT);
                }
                if (previousView != v) v.setBackgroundColor(getResources().getColor(R.color.ci_color_lightest));
                previousView = v;
                return false;
            }
        });
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogFragmentByItem(((BlackBoardItem)
                        blackBoardExpandableListViewAdapter.getGroup(position)));
                return true;
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
    }

    public void showDialogFragmentByItem(BlackBoardItem blackBoardItem){
        blackBoardItemDialogBuilder = BlackBoardItemDialogBuilder.newInstance(blackBoardItem);
        //If the user is able to do anything with the item
        if (blackBoardItemDialogBuilder.hasRights()) {
            blackBoardItemDialogBuilder.show(getFragmentManager(), BlackBoardItemDialogBuilder.class.getSimpleName());
        }
        blackBoardItemDialogBuilder.setOnItemDeleteListener(this);
    }

    @Override
    public void onDelete(BlackBoardItem blackBoardItem) {
        //Close Dialog
        blackBoardItemDialogBuilder.dismiss();
        deletedBlackBoardItem = blackBoardItem;

        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.LIVE_PREVIEW_BLACKBOARD_ITEM_KEY,
                JSONBuilder.createJSONFromObject(blackBoardItem));
        jsonManager.sendJson(ServerURLManager.DELETE_BLACK_BOARD_ITEM_URL, jsonObject);
    }

    @Override
    public void onEdit(BlackBoardItem blackBoardItem) {
        //Close Dialog
        blackBoardItemDialogBuilder.dismiss();
        //Open BlackBoardAddItem with arguments
        Bundle params = new Bundle();
        params.putLong(BlackBoardAddItem.BLACK_BOARD_ITEM_ID_TAG, blackBoardItem.getBlackBoardItemID());

        BlackBoardAddItem blackBoardAddItem = new BlackBoardAddItem();
        blackBoardAddItem.setArguments(params);

        ((BlackBoard)getActivity()).openFragment(blackBoardAddItem, true);
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
