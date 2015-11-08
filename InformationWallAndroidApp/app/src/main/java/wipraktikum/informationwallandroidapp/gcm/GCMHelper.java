package wipraktikum.informationwallandroidapp.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.ServerCommunication.PhpRequestManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

/**
 * Created by Remi on 08.11.2015.
 */
public class GCMHelper {

    private String authorizedEntity = "819997644402" ; // Project id from Google Developer Console
    private String scope = "GCM";
    private String regid;
    private InstanceID instanceID;
    private static GCMHelper instance;
    private Context mContext;

    public GCMHelper(){
        mContext = InfoWallApplication.getInstance();
    }

    public static GCMHelper getInstance(){
        if (instance == null){
            instance = new GCMHelper();
        }
        return instance;
    }

    public void registerToGCM(final String emailAddress){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (instanceID == null) {
                        instanceID = InstanceID.getInstance(mContext);
                    }
                    regid  = instanceID.getToken(authorizedEntity, scope);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);
                    sendIDtoServer(emailAddress, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            public void sendIDtoServer(String emailAddress, String gcmId) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailAddress", emailAddress);
                params.put("gcmId", gcmId);
                PhpRequestManager.getInstance().phpRequest(ServerURLManager.UPDATE_USER, params);
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
    }

    public void unregisterFromGCM(final String emailAddress){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (instanceID == null) {
                        instanceID = InstanceID.getInstance(mContext);
                    }
                    instanceID.deleteToken(authorizedEntity, scope);
                    msg = "Device unregistered";
                    Log.i("GCM", msg);
                    sendRequestToServer(emailAddress);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            public void sendRequestToServer(String emailAddress) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailAddress", emailAddress);
                    PhpRequestManager.getInstance().phpRequest(ServerURLManager.UPDATE_USER, params);
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
    }
}
