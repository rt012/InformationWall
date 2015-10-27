package wipraktikum.informationwallandroidapp.BlackBoard.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 27.10.2015.
 */
public class BlackBoardListViewAttachmentAdapter extends ArrayAdapter<BlackBoardAttachment> {
    private List<BlackBoardAttachment> mAttachments;
    private Context mContext;

    public BlackBoardListViewAttachmentAdapter(Context context, int resource, List<BlackBoardAttachment> attachments) {
        super(context, resource, attachments);
        mAttachments = attachments;
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.black_board_lv_attachment_item, null);
        }

        BlackBoardAttachment attachment = mAttachments.get(position);

        //Image View
        ImageView attachmentIcon = (ImageView) convertView.
                findViewById(R.id.iv_black_board_attachment_item);
        attachmentIcon.setImageDrawable(getDrawableFromDataType(attachment.getDataType()));

        //Attachment Name
        TextView attachmentName = (TextView) convertView.
                findViewById(R.id.tv_black_board_attachment_item);
        File attachmentFile = new File(attachment.getDeviceDataPath());
        attachmentName.setText(attachmentFile.getName());

        return convertView;
    }

    private Drawable getDrawableFromDataType(BlackBoardAttachment.DataType dataType){
        Drawable drawable = null;

        switch (dataType){
            case PDF:
                drawable =  mContext.getDrawable(R.drawable.icon_pdf);
                break;
            case IMG:
                drawable =  mContext.getDrawable(R.drawable.icon_img);
                break;
            default:
                drawable =  mContext.getDrawable(R.drawable.icon_attachment);
                break;
        }

        return  drawable;
    }
}
