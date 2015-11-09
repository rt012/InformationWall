package wipraktikum.informationwallandroidapp.BlackBoard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardExpandableListViewAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.Dialog.BlackBoardItemDialogBuilder;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

public class BlackBoardOverview extends Fragment implements BlackBoardItemDialogBuilder.OnItemChangeListener{
    private final String FRAGMENT_TAG = "FRAGMENT_OVERVIEW";

    private BlackBoardExpandableListViewAdapter blackBoardExpandableListViewAdapter = null;
    private static BlackBoardOverview instance = null;

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
        blackBoardExpandableListViewAdapter.notifyDataSetChanged();
    }

    public void showDialogFragmentByItem(BlackBoardItem blackBoardItem){
        BlackBoardItemDialogBuilder blackBoardItemDialogBuilder = BlackBoardItemDialogBuilder.newInstance(blackBoardItem);
        //If the user is able to do anything with the item
        if (blackBoardItemDialogBuilder.hasRights()) {
            blackBoardItemDialogBuilder.show(getFragmentManager(), BlackBoardItemDialogBuilder.class.getSimpleName());
        }

        blackBoardItemDialogBuilder.setOnItemDeleteListener(this);
    }

    @Override
    public void onDelete(BlackBoardItem blackBoardItem) {
        DAOHelper.getInstance().getBlackBoardItemDAO().delete(blackBoardItem);
        //TODO DELETE FROM SERVER
    }

    @Override
    public void onEdit(BlackBoardItem blackBoardItem) {
        //Open BlackBoardAddItem with arguments
        Bundle params = new Bundle();
        params.putLong(BlackBoardAddItem.BLACK_BOARD_ITEM_ID_TAG, blackBoardItem.getBlackBoardItemID());

        BlackBoardAddItem blackBoardAddItem = new BlackBoardAddItem();
        blackBoardAddItem.setArguments(params);

        ((BlackBoard)getActivity()).openFragment(blackBoardAddItem, true);
        //TODO EDIT FROM SERVER
    }
}
