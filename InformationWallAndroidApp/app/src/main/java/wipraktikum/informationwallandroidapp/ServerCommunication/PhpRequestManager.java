package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 03.11.2015.
 */
public class PhpRequestManager {
    private static PhpRequestManager instance = null;
    private Context mContext = null;

    final String VOLLEY_TAG = "Volley Log";

    private PhpRequestManager(){
        mContext = InfoWallApplication.getInstance();
    }

    public static PhpRequestManager getInstance(){
        if (instance == null){
            instance = new PhpRequestManager();
        }
        return instance;
    }

    public boolean phpRequest(String uploadUrl, final String paramKey, final String paramValue){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        VolleyLog.d(VOLLEY_TAG, "Success: " + s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyLog.d(VOLLEY_TAG, "Error: " + volleyError.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(paramKey, paramValue);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        //Adding request to the queue
        requestQueue.add(stringRequest);
        return false;
    }
}
