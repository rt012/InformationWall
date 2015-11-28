package wipraktikum.informationwallandroidapp.ServerCommunication;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class ServerURLManager {
    //Update blackboard behaviour
    public static final String UPDATE_BLACKBOARD_BEHAVIOUR_URL =  "http://myinfowall.ddns.net/apps/blackboard/updateStream.php";
    //Live Preview BlackBoardItemCreate / Edit
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY = "addBlackboardItem";
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW = "add";
    public static final String SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE = "clear";
    //Open blackboard and put in center
    public static final String OPEN_BLACK_BOARD_PARAM_KEY = "status";
    public static final String OPEN_BLACK_BOARD_PARAM_OPEN = "open";
    public static final String OPEN_BLACK_BOARD_PARAM_CLOSE = "close";
    //Show blackboard tile on informationwall
    public static final String SHOW_BLACK_BOARD_PARAM_KEY = "activated";
    public static final String SHOW_BLACK_BOARD_PARAM_ACTIVE = "active";
    public static final String SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE = "notactivated";
    //Create or update a blackboard item
    public static final String NEW_BLACKBOARD_ITEM_KEY = "newBlackboardItem";
    //Validate user
    public static final String LOG_IN_AUTHENTICATION_KEY = "checkUser";
    //Upload a attachment to the server
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_KEY = "newfile";
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_NAME_KEY = "filename";
    //Get all blackboard items from the server
    public static final String LIVE_PREVIEW_BLACKBOARD_ITEM_KEY = "livePreviewBlackboardItem";
    //Delete BlackBoardItem
    public static final String DELETE_BLACK_BOARD_ITEM_KEY = "deleteBlackBoardItem";

    public static final String GET_ALL_ITEMS_URL = "http://myinfowall.ddns.net/apps/blackboard/getAllBlackBoardItems.php";


}
