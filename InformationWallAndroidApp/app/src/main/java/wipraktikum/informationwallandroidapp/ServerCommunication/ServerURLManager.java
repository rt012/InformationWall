package wipraktikum.informationwallandroidapp.ServerCommunication;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class ServerURLManager {
    public static String OPEN_BLACK_BOARD_URL = "http://myinfowall.ddns.net/apps/blackboard/openBlackBoardOverview.php";
    public static String OPEN_BLACK_BOARD_PARAM_KEY = "status";
    public static String OPEN_BLACK_BOARD_PARAM_OPEN = "open";
    public static String OPEN_BLACK_BOARD_PARAM_CLOSE = "close";

    public static String SHOW_BLACK_BOARD_URL =  "http://myinfowall.ddns.net/apps/blackboard/activateTile.php";
    public static String SHOW_BLACK_BOARD_PARAM_KEY = "activated";
    public static String SHOW_BLACK_BOARD_PARAM_ACTIVE = "active";
    public static String SHOW_BLACK_BOARD_PARAM_NOT_ACTIVE = "notactivated";

    public static final String NEW_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/blackboard/blackBoardGetJSON.php";
    public static final String GET_ALL_ITEMS_URL = "http://myinfowall.ddns.net/apps/blackboard/getAllBlackBoardItems.php";

    public static final String UPLOAD_BLACK_BOARD_ATTACHMENT_URL = "http://myinfowall.ddns.net/phpTest2.php";
}
