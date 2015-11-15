package wipraktikum.informationwallandroidapp.ServerCommunication;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 27.10.2015.
 */
public class JsonManager {
    private static JsonManager instance = null;
    private OnObjectResponseListener mOnObjectResponseListener;
    private OnArrayResponseListener mOnArrayResponseListener;
    private OnErrorListener mOnErrorListener;
    final String VOLLEY_TAG = "Volley Log";

    public void sendJson(String url, Object object) {
        JSONObject objectAsJsonObject = null;
        if(object != null) {
            try {
                Gson gsonHandler = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                String objectAsJsonString = gsonHandler.toJson(object);
                objectAsJsonObject = new JSONObject(objectAsJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,  objectAsJsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(VOLLEY_TAG, "Success: " + response.toString());
                        if(mOnObjectResponseListener != null){
                            mOnObjectResponseListener.OnResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(VOLLEY_TAG, "Error: " + error.getMessage());
                        if(mOnErrorListener != null){
                            mOnErrorListener.OnErrorResponse(error);
                        }
                    }
                }) {
        };

        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }

    public void getJsonArray(String url) {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        VolleyLog.d(VOLLEY_TAG, "Success: " + response.toString());
                        if(mOnArrayResponseListener != null){
                            mOnArrayResponseListener.OnResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(VOLLEY_TAG, "Error: " + error.getMessage());
                if(mOnErrorListener != null){
                    mOnErrorListener.OnErrorResponse(error);
                }
            }
        }) {
        };

        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }

    public void setOnObjectResponseReceiveListener(OnObjectResponseListener onObjectResponseListener){
        mOnObjectResponseListener = onObjectResponseListener;
    }

    public interface OnObjectResponseListener {
        void OnResponse(JSONObject response);
    }

    public void setOnArrayResponseReceiveListener(OnArrayResponseListener onArrayResponseListener){
        mOnArrayResponseListener = onArrayResponseListener;
    }

    public interface OnArrayResponseListener {
        void OnResponse(JSONArray response);
    }

    public void setOnErrorReceiveListener(OnErrorListener onErrorListener){
        mOnErrorListener = onErrorListener;
    }

    public interface OnErrorListener {
        void OnErrorResponse(VolleyError error);
    }
}
