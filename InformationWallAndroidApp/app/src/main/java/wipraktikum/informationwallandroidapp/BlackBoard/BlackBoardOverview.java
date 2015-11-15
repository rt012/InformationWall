package wipraktikum.informationwallandroidapp.BlackBoard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardExpandableListViewAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.Dialog.BlackBoardItemDialogBuilder;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

public class BlackBoardOverview extends Fragment implements BlackBoardItemDialogBuilder.OnItemChangeListener, JsonManager.OnObjectResponseListener {
    private final String FRAGMENT_TAG = "FRAGMENT_OVERVIEW";

    private BlackBoardExpandableListViewAdapter blackBoardExpandableListViewAdapter = null;
    private BlackBoardItemDialogBuilder blackBoardItemDialogBuilder = null;
    private static BlackBoardOverview instance = null;
    private BlackBoardItem deletedBlackBoardItem;
    private JsonManager jsonManager;

    public static BlackBoardOverview getInstance(){
        if (instance==null){
            instance = new BlackBoardOverview();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_board_overview, viewGroup, false);

        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.ex_lv_black_board);
        blackBoardExpandableListViewAdapter = new BlackBoardExpandableListViewAdapter(getActivity());
        expandableListView.setAdapter(blackBoardExpandableListViewAdapter);

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
        }
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
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
        new JsonManager().sendJson(ServerURLManager.DELETE_BLACK_BOARD_ITEM_URL, blackBoardItem);
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
        //TODO EDIT FROM SERVER
    }

    @Override
    public void OnResponse(JSONObject response) {
        DAOHelper.getInstance().getBlackBoardItemDAO().delete(deletedBlackBoardItem);
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
    }
}
