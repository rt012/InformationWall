package wipraktikum.informationwallandroidapp.Utils;

import android.content.Context;
import android.content.Intent;

import wipraktikum.informationwallandroidapp.Login.LoginActivity;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Remi on 15.11.2015.
 */
public class ActivityHelper {
    public static void openLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openTileOverviewActivity(Context context) {
        Intent intent = new Intent(context, TileOverview.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
