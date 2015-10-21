package wipraktikum.informationwallandroidapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Remi on 18.10.2015.
 */
public class GridViewLongClickDialog extends DialogFragment {

    private RadioGroup radioGroup;
    private Switch switchActivate;

    private OnSwitchChangeListener mOnSwitchChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Get arguments
        String tileTitle = getArguments().getString("tileTitle");
        Boolean isActivated = getArguments().getBoolean("isActivated");

        // Inflate and set the layouts for the dialog
        View customDialogView = inflater.inflate(R.layout.dialog_gridviewlongclick, null);
        View customDialogTitle = inflater.inflate(R.layout.dialog_gridviewlongclick_title, null);

        RadioGroup radioGroupSize = (RadioGroup) customDialogView.findViewById(R.id.radioGroup_itemSize);

        Switch switchActivate = (Switch) customDialogTitle.findViewById(R.id.switch_showItem);
        TextView titleText = (TextView) customDialogTitle.findViewById(R.id.dialogTitleText);

        //Use Arguments to configure Dialog (Title, RadioButton, Activated)
        titleText.setText(tileTitle);
        switchActivate.setChecked(isActivated);

        // Define Click handler for views
        switchActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mOnSwitchChangeListener != null){
                    mOnSwitchChangeListener.onSwitchChanged(isChecked);
                }
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customDialogView)
                // Set title
                .setCustomTitle(customDialogTitle)
                // Add action buttons
                .setPositiveButton(R.string.acceptDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("FragmentAlertDialog", "Positive Button");
                    }
                })
                .setNegativeButton(R.string.cancelDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("FragmentAlertDialog", "Negative Button");
                    }
                });
        return builder.create();
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener){
        mOnSwitchChangeListener = onSwitchChangeListener;
    }

    public interface OnSwitchChangeListener{
        public void onSwitchChanged(boolean isChecked);
    }
}