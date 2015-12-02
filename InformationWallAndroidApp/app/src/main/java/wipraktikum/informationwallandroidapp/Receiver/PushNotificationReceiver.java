package wipraktikum.informationwallandroidapp.Receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoard;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;

/**
 * Created by Remi on 11.11.2015.
 */
public class PushNotificationReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = PushNotificationReceiver.class.getSimpleName();

    private NotificationHelper notificationUtils;

    private Intent parseIntent;

    public PushNotificationReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            Gson gsonHandler = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            BlackBoardItem blackBoardItem = gsonHandler.fromJson(json.toString(), BlackBoardItem.class);

            boolean isBackground = false;

            if (!isBackground) {
                Intent resultIntent = new Intent(context, BlackBoard.class);
                DAOHelper.getBlackBoardItemDAO().createOrUpdate(blackBoardItem);
                showNotificationMessage(context, blackBoardItem, resultIntent);
            }

        } catch (Exception e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param intent
     */
    private void showNotificationMessage(Context context, BlackBoardItem blackBoardItem, Intent intent) {

        notificationUtils = NotificationHelper.getInstance();

        intent.putExtras(parseIntent.getExtras());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(blackBoardItem, intent);
    }
}
