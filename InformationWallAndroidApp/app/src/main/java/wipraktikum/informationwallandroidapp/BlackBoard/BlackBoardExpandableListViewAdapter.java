package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
public class BlackBoardExpandableListViewAdapter extends BaseExpandableListAdapter{

    private final Context context;
    private List<BlackBoardItem> mBlackBoardItems = new ArrayList<BlackBoardItem>();
    private Dao<BlackBoardItem, Long> blackBoardItemDAO = null;

    public BlackBoardExpandableListViewAdapter(Context context) {
        this.context = context;

        // Get black board items from database
        try {
            blackBoardItemDAO = OpenHelperManager.getHelper(context,
                    InformationWallORMHelper.class).getBlackBoardItemDAO();
            mBlackBoardItems = blackBoardItemDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPos) {
        return mBlackBoardItems.get(groupPos);
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return mBlackBoardItems.get(groupPos);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        BlackBoardItem blackBoardItem = (BlackBoardItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.black_board_ex_lv_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_title);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(blackBoardItem.getTitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        BlackBoardItem blackBoardItem = (BlackBoardItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.black_board_ex_lv_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_description);

        txtListChild.setText(blackBoardItem.getDescriptionText());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
