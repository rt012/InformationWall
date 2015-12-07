package wipraktikum.informationwallandroidapp.BlackBoard.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 07.12.2015.
 */
public class BlackboardAddWebAttachmentDialog extends DialogFragment {
    //Interfaces
    private OnWebAttachmentInputListener mOnWebAttachmentInputListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layouts for the dialog
        final View customDialogView = inflater.inflate(R.layout.dialog_blackboard_add_web_attachment, null);
        final View customDialogTitle = inflater.inflate(R.layout.dialog_blackboard_add_web_attachment_title, null);

        final EditText etWebURL = (EditText) customDialogView.findViewById(R.id.input_web_url);

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customDialogView)
                // Set title
                .setCustomTitle(customDialogTitle)
                // Add action buttons
                .setPositiveButton(R.string.acceptDialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnWebAttachmentInputListener != null) {
                            mOnWebAttachmentInputListener.onWebAttachmentInput(etWebURL.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.cancelDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("FragmentAlertDialog", "Negative Button");
                    }
                });
        return builder.create();
    }

    public void setOnWebAttachmentInputListener(OnWebAttachmentInputListener onWebAttachmentInputListener){
        mOnWebAttachmentInputListener = onWebAttachmentInputListener;
    }

    public interface OnWebAttachmentInputListener {
        void onWebAttachmentInput(String webURL);
    }
}
