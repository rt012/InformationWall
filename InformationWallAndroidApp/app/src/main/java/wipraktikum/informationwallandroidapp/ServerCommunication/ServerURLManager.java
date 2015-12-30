package wipraktikum.informationwallandroidapp.ServerCommunication;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class ServerURLManager {
    //Change Tile Size
    public static final String CHANGE_TILE_PARAM_URL = "http://myinfowall.ddns.net/apps/tileSettings.php";
    public static final String CHANGE_TILE_SIZE_KEY = "EnumTileSize";
    //Create a new Feed
    public static final String NEW_FEED_KEY = "http://myinfowall.ddns.net/apps/feed/addFeed.php";
    //Create a new Feed
    public static final String DELETE_FEED_KEY = "http://myinfowall.ddns.net/apps/feed/deleteFeed.php";
    //Show blackboard tile on InformationWall
    public static final String SHOW_BLACK_BOARD_PARAM_KEY = "activated";
    public static final String SHOW_BLACK_BOARD_PARAM_ACTIVE = "active";
    public static final String SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE = "notactivated";
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
    //Get all blackboard items from the server
    public static final String LIVE_PREVIEW_BLACKBOARD_ITEM_KEY = "livePreviewBlackboardItem";
    //Create or update a blackboard item
    public static final String NEW_BLACKBOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/blackboard/blackBoardGetJSON.php";
    //Validate user
    public static final String LOG_IN_AUTHENTICATION_URL = "http://myinfowall.ddns.net/apps/blackboard/checkUser.php";
    //Delete BlackBoardItem
    public static final String DELETE_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/blackboard/deleteBlackBoardItem.php";
    //Upload a attachment to the server
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_URL = "http://myinfowall.ddns.net/apps/blackboard/fileUpload.php";
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_KEY = "newfile";
    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_FILE_NAME_KEY = "filename";
    //Sync
    public static final String GET_ALL_BLACKBOARD_ITEMS_URL = "http://myinfowall.ddns.net/apps/blackboard/getAllBlackBoardItems.php";
    public static final String GET_ALL_CONTACTS_URL = "http://myinfowall.ddns.net/apps/blackboard/syncContact.php";

}
