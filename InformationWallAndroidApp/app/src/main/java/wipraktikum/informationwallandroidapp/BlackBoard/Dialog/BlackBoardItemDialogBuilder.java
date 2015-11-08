package wipraktikum.informationwallandroidapp.BlackBoard.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 08.11.2015.
 */
public class BlackBoardItemDialogBuilder extends DialogFragment {
    public final static String BLACK_BOARD_ITEM_ID_KEY = "blackBoardItemID";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Get arguments
        long blackBoardItemID = getArguments().getLong(BLACK_BOARD_ITEM_ID_KEY);

        //Get Objects
        BlackBoardItem blackBoardItem = (BlackBoardItem) DAOHelper.getInstance().
                getBlackBoardItemDAO().queryForId(blackBoardItemID);
        User user = InfoWallApplication.currentUser;

        // Inflate and set the layouts for the dialog
        final View customDialogView = inflater.inflate(R.layout.dialog_blackboarditem_long_click, null);
        final Button btDelete = (Button) customDialogView.findViewById(R.id.bt_black_board_item_delete_dialog);
        final Button btEdit = (Button) customDialogView.findViewById(R.id.bt_black_board_item_edit_dialog);

        //Rights management
        if (!user.getUserGroup().canDelete()){
            btDelete.setVisibility(View.GONE);
        }
        if (!user.getUserGroup().canEdit()){
            btEdit.setVisibility(View.GONE);
        }
        //If the user is the creator than allow edit
        if (blackBoardItem.getUser().equals(user)){
            btEdit.setVisibility(View.VISIBLE);
        }
        //Check if the user is able to do anything
        if (btEdit.getVisibility() != View.GONE || btDelete.getVisibility() != View.GONE) {
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(customDialogView)
                    // Set title
                    .setTitle(getString(R.string.black_board_long_click_dialog_title) + " " + blackBoardItem.getTitle())
                            // Add action buttons
                    .setNegativeButton(R.string.cancelDialog, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("FragmentAlertDialog", "Negative Button");
                        }
                    });
            return builder.create();
        }else{
            return null;
        }
    }
}