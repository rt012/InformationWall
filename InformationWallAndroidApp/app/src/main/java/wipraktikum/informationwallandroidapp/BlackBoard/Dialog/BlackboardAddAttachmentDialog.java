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

import wipraktikum.informationwallandroidapp.BlackBoard.Enum.AttachmentEnum;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 07.12.2015.
 */
public class BlackboardAddAttachmentDialog extends DialogFragment {
    //Interfaces
    private OnAttachmentTypeSelectListener mOnAttachmentTypeSelectListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layouts for the dialog
        final View customDialogView = inflater.inflate(R.layout.dialog_blackboard_add_attachment, null);
        final View customDialogTitle = inflater.inflate(R.layout.dialog_blackboard_add_attachment_title, null);

        final Button btLocalDevice = (Button) customDialogView.findViewById(R.id.ib_blackboard_add_attachment_local);
        final Button btWeb = (Button) customDialogView.findViewById(R.id.ib_blackboard_add_attachment_web);

        btLocalDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnAttachmentTypeSelectListener != null){
                    mOnAttachmentTypeSelectListener.onTypeSelect(AttachmentEnum.LOCAL);
                }
            }
        });
        btWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnAttachmentTypeSelectListener != null){
                    mOnAttachmentTypeSelectListener.onTypeSelect(AttachmentEnum.WEB);
                }
            }
        });

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

    public void setOnAttachmentTypeSelectListener(OnAttachmentTypeSelectListener onAttachmentTypeSelectListener){
        mOnAttachmentTypeSelectListener = onAttachmentTypeSelectListener;
    }

    public interface OnAttachmentTypeSelectListener {
        void onTypeSelect(AttachmentEnum attachmentEnum);
    }

}
