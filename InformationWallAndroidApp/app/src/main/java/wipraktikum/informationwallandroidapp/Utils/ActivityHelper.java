package wipraktikum.informationwallandroidapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import wipraktikum.informationwallandroidapp.Blackboard.Blackboard;
import wipraktikum.informationwallandroidapp.Blackboard.BlackBoardItemLayoutSelection;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.Account.AccountActivity;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Remi on 15.11.2015.
 */
public class ActivityHelper {
    public static void openLoginActivity(Context context){
        Intent intent = new Intent(context, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openBlackboardActivity(Context context) {
        Intent intent = new Intent(context, Blackboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openTileOverviewActivity(Context context) {
        Intent intent = new Intent(context, TileOverview.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openLayoutSelectionActivity(Context context) {
        Intent intent = new Intent(context, BlackBoardItemLayoutSelection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) InfoWallApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
