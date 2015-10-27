package wipraktikum.informationwallandroidapp.BlackBoard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
        ListView lvContactInformation = (ListView) convertView.
                findViewById(R.id.lv_black_board_item_attachment);
        lvContactInformation.setAdapter(new BlackBoardListViewAttachmentAdapter(context, 0, blackBoardItem.getBlackBoardAttachment()));
        //Contact Information
        TextView txtContactCompany = (TextView) convertView
                .findViewById(R.id.tv_black_board_contact_company);
        txtContactCompany.setText(blackBoardItem.getContact().getCompany());
        ImageView ivContactCompany = (ImageView) convertView
                .findViewById(R.id.iv_black_board_contact_company);
        ivContactCompany.setImageDrawable(context.getDrawable(R.drawable.icon_company));

        TextView txtContactFullName= (TextView) convertView
                .findViewById(R.id.tv_black_board_contact_full_name);
        txtContactFullName.setText(blackBoardItem.getContact().getFullName());
        ImageView ivContactFullName = (ImageView) convertView
                .findViewById(R.id.iv_black_board_contact_full_name);
        ivContactFullName.setImageDrawable(context.getDrawable(R.drawable.icon_name));

        TextView txtContactEmail = (TextView) convertView
                .findViewById(R.id.tv_black_board_contact_email);
        txtContactEmail.setText(blackBoardItem.getContact().getEMailAddress());
        ImageView ivContactEmail = (ImageView) convertView
                .findViewById(R.id.iv_black_board_contact_email);
        ivContactEmail.setImageDrawable(context.getDrawable(R.drawable.icon_email));

        TextView txtContactTel= (TextView) convertView
                .findViewById(R.id.tv_black_board_contact_tel);
        txtContactTel.setText(blackBoardItem.getContact().getTelephone());
        ImageView ivContactTel = (ImageView) convertView
                .findViewById(R.id.iv_black_board_contact_tel);
        ivContactTel.setImageDrawable(context.getDrawable(R.drawable.icon_telephone));

        TextView txtContactFullAddress = (TextView) convertView
                .findViewById(R.id.tv_black_board_contact_address);
        txtContactFullAddress.setText(blackBoardItem.getContact().getContactAddress().getFullAddress());
        ImageView ivContactFullAddress = (ImageView) convertView
                .findViewById(R.id.iv_black_board_contact_address);
        ivContactFullAddress.setImageDrawable(context.getDrawable(R.drawable.icon_address));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
