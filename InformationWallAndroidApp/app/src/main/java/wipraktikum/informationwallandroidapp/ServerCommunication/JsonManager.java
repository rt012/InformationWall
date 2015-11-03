package wipraktikum.informationwallandroidapp.ServerCommunication;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 27.10.2015.
 */
public class JsonManager {
    private static JsonManager instance = null;

    final String VOLLEY_TAG = "Volley Log";
    public static final String NEW_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/content/blackboard.php";

    private JsonManager(){}

    public static JsonManager getInstance(){
        if (instance == null){
            instance = new JsonManager();
        }
        return instance;
    }

    public void sendJson(BlackBoardItem blackBoardItem, String url) {
        JSONObject blackBoardItemAsJsonObject = null;
        try {
            Gson gsonHandler = new Gson();
            String blackBoardItemAsJsonString = gsonHandler.toJson(blackBoardItem);
            blackBoardItemAsJsonObject = new JSONObject(blackBoardItemAsJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,  blackBoardItemAsJsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(VOLLEY_TAG, "Success: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(VOLLEY_TAG, "Error: " + error.getMessage());
                    }
                }) {
        };

        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }
}
