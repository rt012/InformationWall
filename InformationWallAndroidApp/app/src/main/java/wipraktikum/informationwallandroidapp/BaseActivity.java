package wipraktikum.informationwallandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import wipraktikum.informationwallandroidapp.About.AboutDialog;
import wipraktikum.informationwallandroidapp.Account.LogInManager;
import wipraktikum.informationwallandroidapp.Preferences.AppPreferences;
import wipraktikum.informationwallandroidapp.Utils.ActivityHelper;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;


public class BaseActivity extends AppCompatActivity implements NotificationHelper.OnNotificationReceiveListener {

    private CoordinatorLayout mRootView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mRootView = (CoordinatorLayout) findViewById(R.id.cl_root_layout);

        //Register Listener
        NotificationHelper.getInstance().setOnNotificationReceiveListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, AppPreferences.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                performLogout();
                return true;
            case R.id.action_about:
                openAboutDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openAboutDialog(){
        new AboutDialog().show(getSupportFragmentManager(), AboutDialog.class.getSimpleName());
    }

    private void performLogout() {
        LogInManager.logOutUser(InfoWallApplication.getCurrentUser());
        openLoginActivity();
    }

    private void openLoginActivity() {
        ActivityHelper.openLoginActivity(this);
    }

    @Override
    public void onNotificationReceive() {
        Snackbar.make(getRootView(), "Das ist ein Test", Snackbar.LENGTH_LONG).show();
    }

    public View getRootView(){
        return mRootView;
    }
}
