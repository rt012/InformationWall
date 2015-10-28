package wipraktikum.informationwallandroidapp.HttpConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 27.10.2015.
 */
public class VolleyTest {
    public void doAction(Context context, BlackBoardItem data) {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://myinfowall.ddns.net/info-wall.php/apps/content/blackboard.php";

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        String blackBoardItemAsJsonString = gsonBuilder.create().toJson(data);
        BlackBoardItem item = gsonBuilder.create().fromJson(blackBoardItemAsJsonString, BlackBoardItem.class);
        JSONObject blackBoardItemAsJsonObject = null;
        try {
            blackBoardItemAsJsonObject = new JSONObject(blackBoardItemAsJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, blackBoardItemAsJsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("asd", response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("asd", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Droider");
                return params;
            }

        };

        // Adding request to request queue
        InfoWallApplication.getInstance().addToRequestQueue(jsonObjReq, "Test");
    }
}
