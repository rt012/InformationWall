package wipraktikum.informationwallandroidapp.Receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.Blackboard.BlackBoard;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.TransientManager;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;

/**
 * Created by Remi on 11.11.2015.
 */
public class PushNotificationReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = PushNotificationReceiver.class.getSimpleName();
    private final String CHANNEL_KEY = "com.parse.Channel";
    private final String DATA_KEY = "com.parse.Data";

    private final String TILE_CHANGE_CHANNEL = "TileChannel";
    private final String NEW_BLACKBOARD_ITEM_CHANNEL = "BlackboardChannel";

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
            JSONObject json = null;
            String channel = intent.getExtras().getString(CHANNEL_KEY);
            json = new JSONObject(intent.getExtras().getString(DATA_KEY));
            //New Blackboard Item
            if(channel.equals(NEW_BLACKBOARD_ITEM_CHANNEL)) {
                Log.e(TAG, "New Blackboard Push received: " + json);
                parseIntent = intent;
                parseNewBlackboardItemJson(context, json);
            //Tile Change
            }else if(channel.equals(TILE_CHANGE_CHANNEL)) {
                Log.e(TAG, "Tile Change Push received: " + json);
                parseTileChangeJson(json);
                InfoWallApplication.getInstance().onPushReceive();
            }

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

    private void parseTileChangeJson(JSONObject json){
        try {
            Gson gsonHandler = new GsonBuilder().create();
            Tile tile = gsonHandler.fromJson(json.toString(), Tile.class);

            tile = TransientManager.keepTransientTileData(tile);
            tile.setSyncStatus(true);
            DAOHelper.getTileDAO().update(tile);
        } catch (Exception e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parseNewBlackboardItemJson(Context context, JSONObject json) {
        try {
            Gson gsonHandler = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            BlackBoardItem blackBoardItem = gsonHandler.fromJson(json.toString(), BlackBoardItem.class);

            boolean isBackground = false;

            if (!isBackground) {
                Intent resultIntent = new Intent(context, BlackBoard.class);
                blackBoardItem.setUser(TransientManager.keepTransientUserData(blackBoardItem.getUser()));
                blackBoardItem.setBlackBoardAttachment(TransientManager.keepTransientAttachmentList(blackBoardItem.getBlackBoardAttachment()));
                blackBoardItem.setSyncStatus(true);
                DAOHelper.getBlackBoardItemDAO().createOrUpdate(blackBoardItem);
                if(blackBoardItem.getUser().getUserID() != InfoWallApplication.getInstance().getCurrentUser().getUserID()) {
                    showNotificationMessage(context, blackBoardItem, resultIntent);
                }
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
        notificationUtils.showNotificationMessage(intent,
                blackBoardItem.getTitle(),
                InfoWallApplication.getInstance().getString(R.string.new_blackboard_item_notification_title)
                );
    }
}
