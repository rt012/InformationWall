package wipraktikum.informationwallandroidapp.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Eric Schmidt on 22.11.2015.
 */
public class AccountLogIn extends Fragment implements LoginManager.OnRequestLoginResponseReceived{
    private static final String TAG = "LoginActivity";
    public static final String USER_ID_TAG = "userID";

    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private EditText mServerURL;
    private CheckBox mAutoLogin;

    private ProgressDialog progressDialog = null;
    private LoginManager mLoginManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_login, viewGroup, false);

        initViews(view);
        initLoginManager();
        setLoginButtonClickListener();
        getUserArguments();
        handleHomeButtonEnabled(view);

        return view;
    }

    private void handleHomeButtonEnabled(View view){
        if (LoginManager.existPreviousAccountData()) {
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void getUserArguments(){
        //Write BlackBoardItem Information to View
        if (getArguments() != null){
            User user = (User) DAOHelper.getUserDAO().queryForId(
                    getArguments().getLong(USER_ID_TAG));
            setUserViewContent(user);
        }
    }

    private void setUserViewContent(User user) {
        mEmailText.setText(user.getEmailAddress());
        mServerURL.setText(user.getServerURL());
    }

    private void initViews(View view) {
        mLoginButton = (Button) view.findViewById(R.id.btn_login);
        mEmailText = (EditText) view.findViewById(R.id.input_email);
        mPasswordText = (EditText) view.findViewById(R.id.input_password);
        mServerURL = (EditText) view.findViewById(R.id.input_server);
        mAutoLogin = (CheckBox) view.findViewById(R.id.checkbox_autoLogin);
    }

    private void initLoginManager() {
        mLoginManager = new LoginManager();
        mLoginManager.setOnRequestLoginResponseReceived(this);
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
            mLoginManager.requestLogin(createUserFromInput(),
                    mAutoLogin.isChecked());
        } else  {
            onLoginFailed();
        }
    }

    private User createUserFromInput(){
        User user = new User();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String serverURL = mServerURL.getText().toString();

        user.setEmailAddress(email);
        user.setPassword(password);
        user.setServerURL(serverURL);

        return user;
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

        if (password.isEmpty()) {
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
        progressDialog = new ProgressDialog(getActivity(),
                R.style.Base_Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.login_authenticating_progress_bar));
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void openTileOverview() {
        Intent intent = new Intent(getActivity(), TileOverview.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onRequestLoginResponseReceived(boolean successful) {
        dismissProgressDialog();
        if(successful) {
            onLoginSuccess();
        } else {
            onLoginFailed();
        }
    }

    private void onLoginSuccess() {
        mLoginButton.setEnabled(true);
        openTileOverview();
    }

    private void onLoginFailed() {
        Snackbar
            .make(getView(), R.string.log_in_failed, Snackbar.LENGTH_LONG)
            .show();
        mLoginButton.setEnabled(true);
    }
}
