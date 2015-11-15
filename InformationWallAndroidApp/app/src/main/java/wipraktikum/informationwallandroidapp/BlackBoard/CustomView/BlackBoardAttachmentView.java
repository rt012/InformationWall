package wipraktikum.informationwallandroidapp.BlackBoard.CustomView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
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

    public BlackBoardAttachmentView(final Context context, final BlackBoardAttachment attachment, boolean isDownloadInProgress) {
        super(context);
        this.mContext = context;
        this.attachment = attachment;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_black_board_lv_attachment_item, this);

        //Attachment Icon
        ImageView attachmentIcon = (ImageView) findViewById(R.id.iv_black_board_attachment_item);
        attachmentIcon.setImageDrawable(getDrawableFromDataType(attachment.getDataType()));

        //Attachment Name
        TextView attachmentName = (TextView)
                findViewById(R.id.tv_black_board_attachment_item);

        if (attachment.getRemoteDataPath() != null) {
            attachmentName.setText(attachment.getName());
        }else{
            attachmentName.setText(attachment.getName());
        }
        //Grey Field if no local Path
        if (!FileHelper.getInstance().exists(attachment.getDeviceDataPath())){
            this.setAlpha(0.5f);
        }

        //Content Loaded
       contentLoadingProgressBar = (ContentLoadingProgressBar)
                findViewById(R.id.lp_black_board_attachment_item);
        if(isDownloadInProgress){
            contentLoadingProgressBar.show();
        }else{
            contentLoadingProgressBar.hide();
        }
    }

    private Drawable getDrawableFromDataType(DBBlackBoardAttachment.DataType dataType) {
        Drawable drawable = null;

        switch (dataType) {
            case PDF:
                drawable = mContext.getDrawable(R.drawable.icon_pdf);
                break;
            case IMG:
                drawable = mContext.getDrawable(R.drawable.icon_img);
                break;
            default:
                drawable = mContext.getDrawable(R.drawable.icon_attachment);
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
}
