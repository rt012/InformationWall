package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.content.Context;

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
    private Context mContext;

    private JsonManager(){
        mContext = InfoWallApplication.getInstance();
    }

    public static JsonManager getInstance(){
        if (instance == null){
            instance = new JsonManager();
        }
        return instance;
    }

    public void doAction(Context context, BlackBoardItem data) {
        final String volleyTag = "Volley Log";

        String url = "http://myinfowall.ddns.net/apps/content/blackboard.php";

        JSONObject blackBoardItemAsJsonObject = null;
        try {
            Gson gsonHandler = new Gson();
            String blackBoardItemAsJsonString = gsonHandler.toJson(data);
            blackBoardItemAsJsonObject = new JSONObject(blackBoardItemAsJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,  blackBoardItemAsJsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(volleyTag, "Success: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(volleyTag, "Error: " + error.getMessage());
                    }
                }) {
        };

        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }
}
