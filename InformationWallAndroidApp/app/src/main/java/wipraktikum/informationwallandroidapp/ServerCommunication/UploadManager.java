package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class UploadManager{
    private static UploadManager instance = null;
    private Context mContext = null;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private UploadManager(){
        mContext = InfoWallApplication.getInstance();
    }

    public static UploadManager getInstance(){
        if (instance == null){
            instance = new UploadManager();
        }
        return instance;
    }

    public boolean uploadFile(final Bitmap uploadImage, String uploadUrl){
        final NotificationHelper notifyHelper= NotificationHelper.getInstance();

        //Create a Notification to indicate Upload
        final int notifyID =  notifyHelper.startNotification(mContext.getString
                (R.string.notification_upload_progress));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        notifyHelper.closeNotification(notifyID);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        notifyHelper.closeNotification(notifyID);
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    String image = getStringImage(uploadImage);

                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put(KEY_IMAGE, image);

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

    private String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
