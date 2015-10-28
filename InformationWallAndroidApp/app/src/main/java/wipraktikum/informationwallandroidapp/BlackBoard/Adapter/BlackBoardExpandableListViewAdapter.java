package wipraktikum.informationwallandroidapp.BlackBoard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardAttachmentView;
import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardContactView;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
public class BlackBoardExpandableListViewAdapter extends BaseExpandableListAdapter{

    private final Context context;
    private List<BlackBoardItem> mBlackBoardItems = new ArrayList<BlackBoardItem>();

    public BlackBoardExpandableListViewAdapter(Context context) {
        this.context = context;

        // Get black board items from database
        mBlackBoardItems = DAOHelper.getInstance().getBlackBoardItemDAO().queryForAll();
    }
    @Override
    public int getGroupCount() {
        return mBlackBoardItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.black_board_ex_lv_group, null);
        }
        //Item Title
        TextView tvItemTitle = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_title);
        tvItemTitle.setText(blackBoardItem.getTitle());
        //Item Information
        TextView tvItemInfo =  (TextView) convertView
                .findViewById(R.id.tv_black_board_item_info);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.mm.yyyy hh:mm:ss");
        String createdTimestamp = formatter.format(blackBoardItem.getCreatedTimestamp());
        tvItemInfo.setText(createdTimestamp + " (" + blackBoardItem.getContact().getFullName() + ")");

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

        //Description Text
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_description);
        txtListChild.setText(blackBoardItem.getDescriptionText());
        //Attachment Information
        LinearLayout attachmentContainer = (LinearLayout) convertView.findViewById(R.id.ll_attachment_container);
        attachmentContainer.removeAllViews();
        for(int i = 0; i < blackBoardItem.getBlackBoardAttachment().size(); i++) {
            BlackBoardAttachmentView attachmentView = new BlackBoardAttachmentView(this.context, blackBoardItem.getBlackBoardAttachment().get(i));
            attachmentContainer.addView(attachmentView);
        }

        LinearLayout contactContainer = (LinearLayout) convertView.findViewById(R.id.ll_contact_container);
        contactContainer.removeAllViews();
        contactContainer.addView(new BlackBoardContactView(this.context, blackBoardItem.getContact()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
