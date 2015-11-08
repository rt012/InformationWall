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
import wipraktikum.informationwallandroidapp.R;

public class BlackBoardOverview extends Fragment implements IFragmentTag{
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

    public String getCustomTag(){
        return this.FRAGMENT_TAG;
    }

    public void showDialogFragmentByItem(BlackBoardItem blackBoardItem){
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong(BlackBoardItemDialogBuilder.BLACK_BOARD_ITEM_ID_KEY,
                blackBoardItem.getBlackBoardItemID());

        BlackBoardItemDialogBuilder blackBoardItemDialogBuilder = new BlackBoardItemDialogBuilder();
        blackBoardItemDialogBuilder.setArguments(args);
        blackBoardItemDialogBuilder.show(getFragmentManager(), "BlackBoardItemDialogBuilder");
    }
}
