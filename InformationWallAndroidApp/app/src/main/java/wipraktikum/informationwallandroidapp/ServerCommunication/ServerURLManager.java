package wipraktikum.informationwallandroidapp.ServerCommunication;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class ServerURLManager {
    public static final String OPEN_BLACK_BOARD_URL = "http://myinfowall.ddns.net/apps/blackboard/openBlackBoardOverview.php";
    public static final String OPEN_BLACK_BOARD_PARAM_KEY = "status";
    public static final String OPEN_BLACK_BOARD_PARAM_OPEN = "open";
    public static final String OPEN_BLACK_BOARD_PARAM_CLOSE = "close";

    public static final String SHOW_BLACK_BOARD_URL =  "http://myinfowall.ddns.net/apps/blackboard/activateTile.php";
    public static final String SHOW_BLACK_BOARD_PARAM_KEY = "activated";
    public static final String SHOW_BLACK_BOARD_PARAM_ACTIVE = "active";
    public static final String SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE = "notactivated";

    public static final String NEW_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/blackboard/blackBoardGetJSON.php";
    public static final String GET_ALL_ITEMS_URL = "http://myinfowall.ddns.net/apps/blackboard/getAllBlackBoardItems.php";

    public static final String LOG_IN_AUTHENTICATION_URL = "http://myinfowall.ddns.net/apps/blackboard/checkUser.php";

    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_URL = "http://myinfowall.ddns.net/apps/blackboard/fileUpload.php";
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_KEY = "newfile";
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_NAME_KEY = "filename";

    //Live Preview BlackBoardItemCreate / Edit
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/blackboard/addBlackboardItem.php";
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY = "addBlackboardItem";
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW = "add";
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE = "clear";

    public static final String LIVE_PREVIEW_BLACK_BOARD_ITEM_JSON_URL = "http://myinfowall.ddns.net/apps/blackboard/addBlackboardGetJSON.php";

    //Delete BlackBoardItem
    public static final String DELETE_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/blackboard/deleteBlackBoardItem.php";

    // Update User with GCM ID
    public static final String UPDATE_USER = "http://myinfowall.ddns.net/updateUser.php";

}
