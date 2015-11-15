package wipraktikum.informationwallandroidapp.Login;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

/**
 * Created by Remi on 14.11.2015.
 */
public class LoginManager {

    private OnRequestLoginResponseReceived mOnRequestLoginResponseReceived;

    public void requestLogin(String email, String password) {
        User loginUser = createRequestedUser(email, password);

        JsonManager jsonManager = new JsonManager();
        jsonManager.sendJson(ServerURLManager.LOG_IN_AUTHENTICATION_URL, loginUser);
        jsonManager.setOnObjectResponseReceiveListener(new JsonManager.OnObjectResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                if (mOnRequestLoginResponseReceived != null) {
                    mOnRequestLoginResponseReceived.OnRequestLoginResponseReceived(true);
                }
                saveUser2DB(response);
            }
        });
        jsonManager.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                if (mOnRequestLoginResponseReceived != null) {
                    mOnRequestLoginResponseReceived.OnRequestLoginResponseReceived(false);
                }
            }
        });
    }

    private User createRequestedUser(String email, String password) {
        return new User(email, password);
    }

    private void saveUser2DB(JSONObject response) {
        User currentUser = new Gson().fromJson(new JsonParser().parse(response.toString()), User.class);
        currentUser.setLoggedIn(true);
        DAOHelper.getInstance().getUserDAO().createOrUpdate(currentUser);
    }

    public void saveLoginInSharedPrefs(String email, String url) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(InfoWallApplication.getInstance());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("loggedIn", true);
        editor.putString("username", email);
        editor.putString("serverURL", url);
        editor.commit();
    }

    public void setOnRequestLoginResponseReceived(OnRequestLoginResponseReceived onRequestLoginResponseReceived){
        this.mOnRequestLoginResponseReceived = onRequestLoginResponseReceived;
    }

    public interface OnRequestLoginResponseReceived {
        void OnRequestLoginResponseReceived(boolean successful);
    }
}
