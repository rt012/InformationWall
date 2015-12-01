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

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 08.11.2015.
 */
public class BlackBoardItemDialogBuilder extends DialogFragment {
    public final static String BLACK_BOARD_ITEM_ID_KEY = "blackBoardItemID";
    //Rights management
    private static boolean canEdit;
    private static boolean canDelete;
    private static boolean hasRights;
    private static BlackBoardItem mBlackBoardItem;
    //Interfaces
    private OnItemChangeListener mOnItemChangeListener;

    public static BlackBoardItemDialogBuilder newInstance(BlackBoardItem blackBoardItem){
        User currentUser = InfoWallApplication.getCurrentUser();
        mBlackBoardItem = blackBoardItem;

        canDelete = false;
        canEdit = false;

        //Rights management
        if (currentUser.getUserGroup().canDelete()){
            canDelete = true;
        }
        if (currentUser.getUserGroup().canEdit()){
            canEdit = true;
        }
        //If the user is the creator than allow edit
        if (blackBoardItem.getUser().equals(currentUser)){
            canEdit = true;
        }

        if (canEdit || canDelete){
            hasRights = true;
        }

        return new BlackBoardItemDialogBuilder();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layouts for the dialog
        final View customDialogView = inflater.inflate(R.layout.dialog_blackboard_item_long_click, null);
        final View customDialogTitle = inflater.inflate(R.layout.dialog_blackboard_item_long_click_title, null);

        final Button btDelete = (Button) customDialogView.findViewById(R.id.bt_black_board_item_delete_dialog);
        final Button btEdit = (Button) customDialogView.findViewById(R.id.bt_black_board_item_edit_dialog);
        final TextView titleText = (TextView) customDialogTitle.findViewById(R.id.dialogTitleText);

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemChangeListener != null){
                    mOnItemChangeListener.onDelete(mBlackBoardItem);
                }
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemChangeListener != null){
                    mOnItemChangeListener.onEdit(mBlackBoardItem);
                }
            }
        });

        titleText.setText(mBlackBoardItem.getTitle());

        //Rights management
        if (!canDelete){
            btDelete.setVisibility(View.GONE);
        }
        if (!canEdit){
            btEdit.setVisibility(View.GONE);
        }
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

    //Must be called before calling show
    public boolean hasRights(){
        return  hasRights;
    }

    public void setOnItemDeleteListener(OnItemChangeListener onItemChangeListener){
        mOnItemChangeListener = onItemChangeListener;
    }

    public interface OnItemChangeListener {
        public void onDelete(BlackBoardItem blackBoardItem);
        public void onEdit(BlackBoardItem blackBoardItem);
    }
}