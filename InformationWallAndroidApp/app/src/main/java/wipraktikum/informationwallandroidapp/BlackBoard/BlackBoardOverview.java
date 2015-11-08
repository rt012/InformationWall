package wipraktikum.informationwallandroidapp.BlackBoard;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardExpandableListViewAdapter;
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

        expandableListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
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
}
