package wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.PhpRequestManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Remi on 22.11.2015.
 */
public class BlackBoardAnimationUtils {

    public static void openLivePreview() {
        Map<String, String> params = new HashMap<>();
        params.put(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW);
        PhpRequestManager.getInstance().phpRequest(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, params);
    }

    public static void closeLivePreviewOnServer(){
        //Tell Server to close Live Preview
        Map<String, String> params = new HashMap<>();
        params.put(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE);

        PhpRequestManager.getInstance().phpRequest(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, params);
    }

    public static void sendLivePreviewUpdate(BlackBoardItem updatedBlackBoardItem) {
        JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY,
                JSONBuilder.createJSONFromObject(updatedBlackBoardItem));

        new JsonManager().sendJson(ServerURLManager.LIVE_PREVIEW_BLACK_BOARD_ITEM_JSON_URL, jsonObject);
    }
}
