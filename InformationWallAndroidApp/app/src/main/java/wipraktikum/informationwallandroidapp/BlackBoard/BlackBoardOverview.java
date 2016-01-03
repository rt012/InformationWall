package wipraktikum.informationwallandroidapp.BlackBoard;

import android.app.ProgressDialog;
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

import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardExpandableListViewAdapter;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation.SyncManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.TransientManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;
import wipraktikum.informationwallandroidapp.Utils.UndoDeleteHelper;

public class BlackBoardOverview extends Fragment implements BlackBoardExpandableListViewAdapter.OnItemChangeListener{
    private final String FRAGMENT_TAG = "FRAGMENT_OVERVIEW";

    private BlackBoardExpandableListViewAdapter blackBoardExpandableListViewAdapter = null;
    private UndoDeleteHelper undoDeleteHelper = null;
    private JsonManager jsonManager;

    private ProgressDialog progressDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blackboard_overview, viewGroup, false);

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
        }

        undoDeleteHelper = new UndoDeleteHelper(((Blackboard)getActivity()).getRootView(), getActivity());

        initViews(getView());
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
                syncManager.syncBlackboardInformation();
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
        DAOHelper.getBlackBoardItemDAO().delete(blackBoardItem);
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();

        jsonManager.sendJson(ServerURLManager.DELETE_BLACK_BOARD_ITEM_URL, JSONBuilder.createJSONFromObject(blackBoardItem));

        undoDeleteHelper.showUndoSnackbar(blackBoardItem);
        undoDeleteHelper.setOnUndoDeleteListener(new UndoDeleteHelper.OnUndoDeleteListener() {
            @Override
            public void onUndo(ArrayList<Object> undoObjects) {
                for (Object object : undoObjects) {
                    BlackBoardItem blackBoardItem = (BlackBoardItem) object;
                    restoreBlackBoardItemInDB(blackBoardItem);
                    restoreBlackBoardItemOnServer(blackBoardItem);
                    blackBoardExpandableListViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void restoreBlackBoardItemInDB(BlackBoardItem blackBoardItem){
        DAOHelper.getBlackBoardItemDAO().createOrUpdate(blackBoardItem);
    }

    private void restoreBlackBoardItemOnServer(final BlackBoardItem blackBoardItem) {
        showProgressDialog();
        JsonManager jsonManager = new JsonManager();
        jsonManager.setOnObjectResponseReceiveListener(new JsonManager.OnObjectResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                updateBlackBoardItemInDB(response, blackBoardItem);
                hideProgressDialog();
            }
        });
        jsonManager.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                ((Blackboard)getActivity()).showSnackBar(R.string.black_board_add_item_snackbar_connection_error);
                hideProgressDialog();
            }
        });

        jsonManager.sendJson(ServerURLManager.NEW_BLACKBOARD_ITEM_URL, JSONBuilder.createJSONFromObject(blackBoardItem));
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.progress_pleaseWait));
        progressDialog.setMessage(getString(R.string.progress_itemUpload));
        progressDialog.show();
    }

    private void hideProgressDialog(){
        progressDialog.hide();
    }

    private void updateBlackBoardItemInDB(JSONObject response, BlackBoardItem blackBoardItem) {
        BlackBoardItem serverBlackBoardItem = BlackBoardItem.parseItemFromJson(response.toString());
        serverBlackBoardItem.setUser(TransientManager.keepTransientUserData(serverBlackBoardItem.getUser()));
        serverBlackBoardItem.setBlackBoardAttachment(TransientManager.keepTransientAttachmentList(serverBlackBoardItem.getBlackBoardAttachment()));
        serverBlackBoardItem.setSyncStatus(true);
        DAOHelper.getBlackBoardItemDAO().delete(blackBoardItem);
        DAOHelper.getBlackBoardItemDAO().createOrUpdate(serverBlackBoardItem);
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
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
}
