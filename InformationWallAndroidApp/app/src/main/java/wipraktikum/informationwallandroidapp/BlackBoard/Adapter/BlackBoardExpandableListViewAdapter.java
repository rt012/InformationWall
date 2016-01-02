package wipraktikum.informationwallandroidapp.BlackBoard.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wipraktikum.informationwallandroidapp.BlackBoard.Blackboard;
import wipraktikum.informationwallandroidapp.BlackBoard.CustomView.BlackBoardAttachmentView;
import wipraktikum.informationwallandroidapp.BlackBoard.CustomView.BlackBoardContactView;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.DownloadManager;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.StringHelper;

/**
 * Created by Eric Schmidt on 25.10.2015.
 */
public class BlackBoardExpandableListViewAdapter extends BaseExpandableListAdapter implements View.OnClickListener{

    private final Context context;
    private List<BlackBoardItem> mBlackBoardItems = new ArrayList<BlackBoardItem>();
    private ArrayList<BlackBoardAttachment> downloadAttachments = new ArrayList<>();
    //Rightsmanagement
    private boolean canEdit = true;
    private boolean canDelete = true;
    //Interface
    private OnItemChangeListener mOnItemChangeListener;

    public BlackBoardExpandableListViewAdapter(Context context) {
        this.context = context;

        // Get black board items from database
        mBlackBoardItems = DAOHelper.getBlackBoardItemDAO().queryForAll();
        sortItems();
    }

    @Override
    public void notifyDataSetChanged() {
        mBlackBoardItems = DAOHelper.getBlackBoardItemDAO().queryForAll();
        sortItems();
        super.notifyDataSetChanged();
    }

    private void sortItems(){
        Collections.sort(mBlackBoardItems, new Comparator<BlackBoardItem>() {
            @Override
            public int compare(BlackBoardItem lhs, BlackBoardItem rhs) {
                return rhs.getEditedTimestamp().compareTo(lhs.getEditedTimestamp());
            }
        });
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

    private void checkUserRights(BlackBoardItem blackBoardItem){
        User currentUser = InfoWallApplication.getCurrentUser();

        canDelete = false;
        canEdit = false;

        //Rights management
        if (currentUser.getUserGroup().canDelete()){
            canDelete = true;
        }
        if (currentUser.getUserGroup().canEdit()){
            canEdit = true;
        }
        //If the user is the creator than allow edit
        if (blackBoardItem.getUser().equals(currentUser)){
            canEdit = true;
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final BlackBoardItem blackBoardItem = (BlackBoardItem) getGroup(groupPosition);
        checkUserRights(blackBoardItem);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.blackboard_item, null);
            //Deactivate Swipe if User has no Rights
            if (!canDelete && !canEdit) ((SwipeLayout) convertView).setSwipeEnabled(false);
        }

        //Edit & Delete
        LinearLayout deleteFeed = (LinearLayout) convertView.findViewById(R.id.delete_object);
        deleteFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerOnDeleteFeedEvent(blackBoardItem);
            }
        });
        if (!canDelete) deleteFeed.setVisibility(View.GONE);

        LinearLayout editFeed = (LinearLayout) convertView.findViewById(R.id.edit_object);
        editFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerOnEditFeedEvent(blackBoardItem);
            }
        });
        if (!canEdit) editFeed.setVisibility(View.GONE);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        //Item Title
        TextView tvItemTitle = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_title);
        tvItemTitle.setText(blackBoardItem.getTitle());
        //Item last edited
        TextView tvItemLastEdited = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_last_edited);

        if(blackBoardItem.getEditedTimestamp() != null) {
            String editedTimestamp = formatter.format(blackBoardItem.getEditedTimestamp());
            tvItemLastEdited.setText(context.getString(R.string.black_board_last_edited) + " " + editedTimestamp);
        }
        //Item Information
        TextView tvItemInfo =  (TextView) convertView
                .findViewById(R.id.tv_black_board_item_info);
        if(blackBoardItem.getCreatedTimestamp() != null) {
            String createdTimestamp = formatter.format(blackBoardItem.getCreatedTimestamp());
            String informationText = createdTimestamp;
            if (blackBoardItem.getContact() != null && !StringHelper.isStringNullOrEmpty(blackBoardItem.getContact().getFullName())){
                informationText += " (" + blackBoardItem.getContact().getFullName() + ")";
            }
            tvItemInfo.setText(informationText);
        }

        return convertView;
    }

    private void triggerOnDeleteFeedEvent(BlackBoardItem blackBoardItem){
        if(mOnItemChangeListener != null){
            mOnItemChangeListener.onDelete(blackBoardItem);
        }
    }

    private void triggerOnEditFeedEvent(BlackBoardItem blackBoardItem){
        if(mOnItemChangeListener != null){
            mOnItemChangeListener.onEdit(blackBoardItem);
        }
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final BlackBoardItem blackBoardItem = (BlackBoardItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.blackboard_ex_lv_child, null);
        }

        //Description Text
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tv_black_board_item_description);
        if(blackBoardItem.getDescriptionText() != null) {
            txtListChild.setText(blackBoardItem.getDescriptionText());
        }
        ViewGroup.LayoutParams params = txtListChild.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        txtListChild.setLayoutParams(params);

        //Attachment Information
        LinearLayout attachmentContainer = (LinearLayout) convertView.findViewById(R.id.ll_attachment_container);
        attachmentContainer.removeAllViews();
        for(int i = 0; i < blackBoardItem.getBlackBoardAttachment().size(); i++) {
            final BlackBoardAttachment attachment = blackBoardItem.getBlackBoardAttachment().get(i);
            BlackBoardAttachmentView attachmentView = new BlackBoardAttachmentView(this.context, attachment,
                    isDownloadInProgress(attachment), false);
            attachmentView.setOnClickListener(this);
            attachmentContainer.addView(attachmentView);
        }

        //Contact Information
        LinearLayout contactContainer = (LinearLayout) convertView.findViewById(R.id.ll_contact_container);
        contactContainer.removeAllViews();
        contactContainer.addView(new BlackBoardContactView(this.context, blackBoardItem.getContact()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void onClick(View v) {
        final BlackBoardAttachmentView convertView = (BlackBoardAttachmentView) v;
        final BlackBoardAttachment attachment = convertView.getItem();
        final FileHelper fileHelper = FileHelper.getInstance();
        //Download the file if does not exist
        if (!fileHelper.isURL(attachment.getDeviceDataPath()) && !fileHelper.exists(attachment.getDeviceDataPath())) {
            convertView.showProgressbar(true);
            downloadAttachments.add(convertView.getItem());
            //Start Download
            final String filePath = DownloadManager.getInstance().downloadFile(attachment.getRemoteDataPath());

            //Something went wrong downloading the file
            if (filePath == null){
                convertView.showProgressbar(false);
                downloadAttachments.remove(convertView.getItem());
                ((Blackboard) context).showSnackBar(R.string.blackboard_overview_failed_download);
            }

            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    convertView.showProgressbar(false);
                    downloadAttachments.remove(convertView.getItem());
                    //Update BlackBoardAttachment
                    attachment.setDeviceDataPath(filePath);
                    DAOHelper.getBlackBoardAttachmentDAO().update(attachment);
                    //Open File
                    fileHelper.openFile(context, filePath);
                    notifyDataSetChanged();
                }
            }, new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else{
            //Open File
            fileHelper.openFile(context, attachment.getDeviceDataPath());
        }
    }

    private boolean isDownloadInProgress(BlackBoardAttachment attachment){
        boolean inProgress = false;
        if(downloadAttachments.contains(attachment)){
            inProgress = true;
        }
        return inProgress;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener){
        mOnItemChangeListener = onItemChangeListener;
    }

    public interface OnItemChangeListener {
        void onDelete(BlackBoardItem blackBoardItem);
        void onEdit(BlackBoardItem blackBoardItem);
    }
}
