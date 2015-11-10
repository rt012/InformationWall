package wipraktikum.informationwallandroidapp.Login;

/**
 * Created by Remi on 05.11.2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private EditText mServerURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mEmailText = (EditText) findViewById(R.id.input_email);
        mPasswordText = (EditText) findViewById(R.id.input_password);
        mServerURL = (EditText) findViewById(R.id.input_server);

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.login_authenticating_progress_bar));
        progressDialog.show();

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        User loginUser = new User();
        loginUser.setEmailAddress(email);
        loginUser.setPassword(password);

        JsonManager.getInstance().sendJson(ServerURLManager.LOG_IN_AUTHENTICATION_URL, loginUser);
        JsonManager.getInstance().setOnObjectResponseReceiveListener(new JsonManager.OnObjectResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                progressDialog.dismiss();
                User currentUser = new Gson().fromJson(new JsonParser().parse(response.toString()),User.class );
                currentUser.setLoggedIn(true);
                DAOHelper.getInstance().getUserDAO().update(currentUser);
                onLoginSuccess();
            }
        });
        JsonManager.getInstance().setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnResponse(VolleyError error) {
                progressDialog.dismiss();
                onLoginFailed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        mLoginButton.setEnabled(true);
        saveLoginInSharedPrefs();
        Intent intent = new Intent(getApplicationContext(), TileOverview.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mLoginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String serverURL = mServerURL.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        if(serverURL.isEmpty()) {
            mServerURL.setError("enter a valid server URL");
            valid = false;
        } else {
            mServerURL.setError(null);
        }

        return valid;
    }

    private void saveLoginInSharedPrefs() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("loggedIn", true);
        editor.putString("username", mEmailText.getText().toString());
        editor.putString("serverURL",mServerURL.getText().toString());
        editor.commit();
    }
}