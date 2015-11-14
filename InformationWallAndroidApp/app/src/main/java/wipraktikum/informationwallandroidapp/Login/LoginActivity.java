package wipraktikum.informationwallandroidapp.Login;

/**
 * Created by Remi on 05.11.2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

public class LoginActivity extends BaseActivity implements LoginManager.OnRequestLoginResponsReceived {
    private static final String TAG = "LoginActivity";

    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private EditText mServerURL;
    private CheckBox mAutoLogin;

    private ProgressDialog progressDialog = null;
    private LoginManager mLoginManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initLoginManager();
        setLoginButtonClickListener();
    }

    private void initViews() {
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mEmailText = (EditText) findViewById(R.id.input_email);
        mPasswordText = (EditText) findViewById(R.id.input_password);
        mServerURL = (EditText) findViewById(R.id.input_server);
        mAutoLogin = (CheckBox) findViewById(R.id.checkbox_autoLogin);
    }

    private void initLoginManager() {
        mLoginManager = new LoginManager();
    }

    private void setLoginButtonClickListener() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
        mLoginButton.setEnabled(false);
        if (validateInputFields()) {
            showProgressDialog();
            mLoginManager.requestLogin(mEmailText.getText().toString(), mPasswordText.getText().toString());
        } else  {
            onLoginFailed();
        }
    }

    public boolean validateInputFields() {
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

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.login_authenticating_progress_bar));
        progressDialog.show();
    }

    private void dissmissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void openTileOverview() {
        Intent intent = new Intent(getApplicationContext(), TileOverview.class);
        startActivity(intent);
    }

    @Override
    public void OnRequestLoginResponsReceived(boolean successfull) {
        dissmissProgressDialog();
        if(successfull) {
            onLoginSuccess();
        } else {
            onLoginFailed();
        }
    }

    private void onLoginSuccess() {
        mLoginButton.setEnabled(true);
        if(mAutoLogin.isChecked()) {
            mLoginManager.saveLoginInSharedPrefs(mEmailText.getText().toString(), mServerURL.getText().toString());
        }
        openTileOverview();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }
}