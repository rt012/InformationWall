package wipraktikum.informationwallandroidapp.BlackBoard.CustomView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;

/**
 * Created by Remi on 28.10.2015.
 */
public class BlackBoardAttachmentView extends LinearLayout {

    private Context mContext;
    private BlackBoardAttachment attachment;
    private ContentLoadingProgressBar contentLoadingProgressBar = null;

    private OnItemChangeListener mOnItemChangeListener = null;

    public BlackBoardAttachmentView(final Context context, final BlackBoardAttachment attachment
            , boolean isDownloadInProgress, boolean swipeEnabled) {
        super(context);
        this.mContext = context;
        this.attachment = attachment;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.attachment_item, this);

        LinearLayout deleteAttachment = (LinearLayout) findViewById(R.id.delete_object);
        deleteAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerOnDeleteAttachmentEvent(attachment);
            }
        });
        if (!swipeEnabled) deleteAttachment.setVisibility(View.GONE);

        //Edit Field
        LinearLayout editAttachment = (LinearLayout) findViewById(R.id.edit_object);
        editAttachment.setVisibility(View.GONE);

        //Attachment Icon
        ImageView attachmentIcon = (ImageView) findViewById(R.id.iv_black_board_attachment_item);
        attachmentIcon.setImageDrawable(getDrawableFromDataType(attachment.getDataType()));

        //Attachment Name
        TextView attachmentName = (TextView)
                findViewById(R.id.tv_black_board_attachment_item);

        attachmentName.setText(attachment.getAttachmentName());

        //Grey Field if no local Path
        if (!FileHelper.getInstance().isURL(attachment.getDeviceDataPath()) && !FileHelper.getInstance().exists(attachment.getDeviceDataPath())){
            this.setAlpha(0.5f);
        }

        //if swipe is enabled than the item is currently edited = Always not greyed out! (I know there is prop. a better way..)
        this.setAlpha(1F);

        //Content Loaded
       contentLoadingProgressBar = (ContentLoadingProgressBar)
                findViewById(R.id.lp_black_board_attachment_item);
        if(isDownloadInProgress){
            contentLoadingProgressBar.show();
        }else{
            contentLoadingProgressBar.hide();
        }
    }

    private void triggerOnDeleteAttachmentEvent(BlackBoardAttachment attachment) {
        if (mOnItemChangeListener != null) {
            mOnItemChangeListener.onDelete(attachment);
        }
    }

    private Drawable getDrawableFromDataType(DBBlackBoardAttachment.DataType dataType) {
        Drawable drawable = null;

        switch (dataType) {
            case PDF:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_pdf);
                break;
            case IMG:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_img);
                break;
            case VIDEO:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_video);
                break;
            default:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_attachment);
                break;
        }
        return drawable;

    }

    public BlackBoardAttachment getItem(){
        return attachment;
    }

    public void showProgressbar(boolean show){
        if (show) {
            contentLoadingProgressBar.show();
        }else {
            contentLoadingProgressBar.hide();
        }
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener){
        mOnItemChangeListener = onItemChangeListener;
    }

    public interface OnItemChangeListener {
        void onDelete(BlackBoardAttachment blackboardAttachment);
    }
}
