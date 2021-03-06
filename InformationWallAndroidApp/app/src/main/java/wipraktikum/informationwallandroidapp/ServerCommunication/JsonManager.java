package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
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

    public void sendJson(String url, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,  jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.e(VOLLEY_TAG, "Success: " + response.toString());
                        Log.e(VOLLEY_TAG,"Success: " + response.toString());
                        if(mOnObjectResponseListener != null){
                            mOnObjectResponseListener.OnResponse(response);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(VOLLEY_TAG, "Error: " + error.getMessage());
                        Log.e(VOLLEY_TAG, "Error: " + error.getMessage());
                        if(mOnErrorListener != null){
                            mOnErrorListener.OnErrorResponse(error);
                        }
                    }
                }) {
        };
        VolleyLog.e(VOLLEY_TAG, "add to request queue url: " + url + jsonObject.toString());
        Log.e(VOLLEY_TAG, "add to request queue url: " + url + jsonObject.toString());
        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }

    public void getJson(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,  null,
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
