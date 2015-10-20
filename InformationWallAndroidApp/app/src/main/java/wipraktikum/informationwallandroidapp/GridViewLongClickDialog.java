package wipraktikum.informationwallandroidapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.RadioGroup;
import android.widget.Switch;

/**
 * Created by Remi on 18.10.2015.
 */
public class GridViewLongClickDialog extends DialogFragment {

    private RadioGroup radioGroup;
    private Switch switchActivate;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_gridviewlongclick, null))
                // Set title
                .setCustomTitle(inflater.inflate(R.layout.dialog_gridviewlongclick_title, null))
                // Add action buttons
                .setPositiveButton(R.string.acceptDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancelDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }

}