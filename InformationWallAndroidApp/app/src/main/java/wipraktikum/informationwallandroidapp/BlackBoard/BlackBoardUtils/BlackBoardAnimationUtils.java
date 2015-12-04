package wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardUtils;

import android.preference.PreferenceManager;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Remi on 22.11.2015.
 */
public class BlackBoardAnimationUtils {

    public static void openLivePreview() {
        if( PreferenceManager.getDefaultSharedPreferences(InfoWallApplication.getInstance()).getBoolean(InfoWallApplication.getInstance().getString(R.string.pref_animation_key), false)) {
            JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW);
            new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
        }
    }

    public static void closeLivePreviewOnServer(){
        if( PreferenceManager.getDefaultSharedPreferences(InfoWallApplication.getInstance()).getBoolean(InfoWallApplication.getInstance().getString(R.string.pref_animation_key), false)) {
            JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE);
            new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
        }
    }

    public static void sendLivePreviewUpdate(BlackBoardItem updatedBlackBoardItem) {
        if(PreferenceManager.getDefaultSharedPreferences(InfoWallApplication.getInstance()).getBoolean(InfoWallApplication.getInstance().getString(R.string.pref_animation_key), false)) {
            JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.LIVE_PREVIEW_BLACKBOARD_ITEM_KEY,
                    JSONBuilder.createJSONFromObject(updatedBlackBoardItem));

            new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
        }
    }

    public static void openBlackBoardOnServer(String actionParam) {
        if(PreferenceManager.getDefaultSharedPreferences(InfoWallApplication.getInstance()).getBoolean(InfoWallApplication.getInstance().getString(R.string.pref_animation_key), false)) {
            JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.OPEN_BLACK_BOARD_PARAM_KEY, actionParam);
            new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
        }
    }
}
