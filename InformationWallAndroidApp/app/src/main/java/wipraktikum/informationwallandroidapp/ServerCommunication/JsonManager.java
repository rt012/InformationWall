package wipraktikum.informationwallandroidapp.ServerCommunication;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 27.10.2015.
 */
public class JsonManager {
    private static JsonManager instance = null;
    private OnResponseListener mOnResponseListener;
    private OnErrorListener mOnErrorListener;
    final String VOLLEY_TAG = "Volley Log";
    public static final String NEW_BLACK_BOARD_ITEM_URL = "http://myinfowall.ddns.net/apps/content/blackboard.php";
    public static final String GET_ALL_ITEMS_URL = "http://myinfowall.ddns.net/apps/content/getALLBlackBoardItems.php";

    private JsonManager(){}

    public static JsonManager getInstance(){
        if (instance == null){
            instance = new JsonManager();
        }
        return instance;
    }

    public void sendJson(String url, Object object) {
        JSONObject objectAsJsonObject = null;
        if(object != null) {
            try {
                Gson gsonHandler = new Gson();
                String blackBoardItemAsJsonString = gsonHandler.toJson(object);
                objectAsJsonObject = new JSONObject(blackBoardItemAsJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,  objectAsJsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(VOLLEY_TAG, "Success: " + response.toString());
                        if(mOnResponseListener != null){
                            mOnResponseListener.OnResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(VOLLEY_TAG, "Error: " + error.getMessage());
                        if(mOnErrorListener != null){
                            mOnErrorListener.OnResponse(error);
                        }
                    }
                }) {
        };

        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }

    public void setOnResponseReceiveListener(OnResponseListener onResponseListener){
        mOnResponseListener = onResponseListener;
    }

    public interface OnResponseListener {
        void OnResponse(JSONObject response);
    }

    public void setOnErrorReceiveListener(OnErrorListener onErrorListener){
        mOnErrorListener = onErrorListener;
    }

    public interface OnErrorListener {
        void OnResponse(VolleyError error);
    }
}
