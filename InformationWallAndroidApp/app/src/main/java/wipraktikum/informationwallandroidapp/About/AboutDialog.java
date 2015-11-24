package wipraktikum.informationwallandroidapp.About;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 24.11.2015.
 */
public class AboutDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layouts for the dialog
        final View customDialogView = inflater.inflate(R.layout.dialog_about, null);
        final View customDialogTitle = inflater.inflate(R.layout.dialog_about_title, null);

        final TextView tvUser = (TextView) customDialogView.findViewById(R.id.tv_about_user);
        final TextView tvServer = (TextView) customDialogView.findViewById(R.id.tv_about_server);

        User currentUser = InfoWallApplication.getCurrentUser();

        tvUser.setText(currentUser.getEmailAddress());
        tvServer.setText(currentUser.getServerURL());

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
}
