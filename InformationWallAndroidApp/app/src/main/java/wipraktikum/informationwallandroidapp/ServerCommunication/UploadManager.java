package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class UploadManager{
    private static UploadManager instance = null;
    private Context mContext = null;

    private String KEY_FILE = "file";
    private String KEY_NAME = "fileName";

    private OnUploadFinishedListener mOnUploadFinishedListener = null;

    private UploadManager(){
        mContext = InfoWallApplication.getInstance();
    }

    public static UploadManager getInstance(){
        if (instance == null){
            instance = new UploadManager();
        }
        return instance;
    }

    public boolean uploadFile(final File uploadFile, String uploadUrl){
        final NotificationHelper notifyHelper= NotificationHelper.getInstance();

        //Create a Notification to indicate Upload
        final int notifyID =  notifyHelper.startNotification(mContext.getString
                (R.string.notification_upload_progress));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        notifyHelper.closeNotification(notifyID);
                        if(mOnUploadFinishedListener != null){
                            mOnUploadFinishedListener.onUploadFinished(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        notifyHelper.closeNotification(notifyID);
                        if(mOnUploadFinishedListener != null){
                            mOnUploadFinishedListener.onUploadFinished(false);
                        }
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    String image = getStringFile(uploadFile);

                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put(KEY_FILE, image);
                    params.put(KEY_NAME, FileHelper.getInstance().getFileName(uploadFile.getAbsolutePath()));

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

    private String getStringFile(File file){
        String encodedFile = null;
        try {
            byte[] bytes = loadFile(file);
            encodedFile = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedFile;
    }

    private byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    public void setOnUploadFinishedListener(OnUploadFinishedListener onUploadFinishedListener){
        mOnUploadFinishedListener = onUploadFinishedListener;
    }

    public interface OnUploadFinishedListener{
        void onUploadFinished(boolean success);
    }
}
