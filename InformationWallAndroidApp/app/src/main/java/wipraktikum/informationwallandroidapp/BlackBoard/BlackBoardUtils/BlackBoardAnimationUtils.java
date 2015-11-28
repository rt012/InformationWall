package wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardUtils;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Remi on 22.11.2015.
 */
public class BlackBoardAnimationUtils {

    public static void openLivePreview() {
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW);
        new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
    }

    public static void closeLivePreviewOnServer(){
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE);
        new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
    }

    public static void sendLivePreviewUpdate(BlackBoardItem updatedBlackBoardItem) {
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.LIVE_PREVIEW_BLACKBOARD_ITEM_KEY,
                JSONBuilder.createJSONFromObject(updatedBlackBoardItem));

        new JsonManager().sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
    }
}
