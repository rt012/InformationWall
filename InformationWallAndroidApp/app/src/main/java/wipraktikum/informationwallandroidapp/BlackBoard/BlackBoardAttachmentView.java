package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Remi on 28.10.2015.
 */
public class BlackBoardAttachmentView extends LinearLayout {

    private Context mContext;

    public BlackBoardAttachmentView(Context context, BlackBoardAttachment attachment) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.black_board_lv_attachment_item, this);
        ImageView attachmentIcon = (ImageView) findViewById(R.id.iv_black_board_attachment_item);
        attachmentIcon.setImageDrawable(getDrawableFromDataType(attachment.getDataType()));

        //Attachment Name
        TextView attachmentName = (TextView)
                findViewById(R.id.tv_black_board_attachment_item);
        File attachmentFile = new File(attachment.getDeviceDataPath());
        attachmentName.setText(attachmentFile.getName());

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
}
