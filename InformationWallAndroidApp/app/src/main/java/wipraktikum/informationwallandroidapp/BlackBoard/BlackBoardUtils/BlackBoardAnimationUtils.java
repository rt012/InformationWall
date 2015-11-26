package wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardUtils;

import java.util.HashMap;
import java.util.Map;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.PhpRequestManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

/**
 * Created by Remi on 22.11.2015.
 */
public class BlackBoardAnimationUtils {

    public static void openLivePreview() {
        Map<String, String> params = new HashMap<>();
        params.put(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW);
        PhpRequestManager.getInstance().phpRequest(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_URL, params);
    }

    public static void closeLivePreviewOnServer(){
        //Tell Server to close Live Preview
        Map<String, String> params = new HashMap<>();
        params.put(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE);

        PhpRequestManager.getInstance().phpRequest(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_URL, params);
    }

    public static void sendLivePreviewUpdate(BlackBoardItem updatedBlackBoardItem) {
        new JsonManager().sendJson(ServerURLManager.LIVE_PREVIEW_BLACK_BOARD_ITEM_JSON_URL, updatedBlackBoardItem);
    }
}
