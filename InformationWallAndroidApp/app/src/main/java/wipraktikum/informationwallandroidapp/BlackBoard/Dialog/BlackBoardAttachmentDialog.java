package wipraktikum.informationwallandroidapp.BlackBoard.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 01.12.2015.
 */
public class BlackBoardAttachmentDialog extends DialogFragment{
    //Interfaces
    private OnItemChangeListener mOnItemChangeListener;
    private static BlackBoardAttachment mBlackboardAttachment = null;

    public static BlackBoardAttachmentDialog newInstance(BlackBoardAttachment blackboardAttachment){
        mBlackboardAttachment = blackboardAttachment;
        return new BlackBoardAttachmentDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layouts for the dialog
        final View customDialogView = inflater.inflate(R.layout.dialog_blackboard_attachment_long_click, null);
        final View customDialogTitle = inflater.inflate(R.layout.dialog_blackboard_attachment_long_click_title, null);

        final Button btDelete = (Button) customDialogView.findViewById(R.id.bt_black_board_attachment_delete_dialog);
        final TextView titleText = (TextView) customDialogTitle.findViewById(R.id.dialogTitleText);

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemChangeListener != null){
                    mOnItemChangeListener.onDelete(mBlackboardAttachment);
                }
            }
        });

        titleText.setText(mBlackboardAttachment.getName());

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customDialogView)
                // Set title
                .setCustomTitle(customDialogTitle)
                        // Add action buttons
                .setNegativeButton(R.string.cancelDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("FragmentAlertDialog", "Negative Button");
                    }
                });
        return builder.create();
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener){
        mOnItemChangeListener = onItemChangeListener;
    }

    public interface OnItemChangeListener {
        void onDelete(BlackBoardAttachment blackboardAttachment);
    }

}
