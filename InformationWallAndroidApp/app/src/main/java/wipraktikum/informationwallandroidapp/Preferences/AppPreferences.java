package wipraktikum.informationwallandroidapp.Preferences;

/**
 * Created by Remi on 12.11.2015.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.ParseUtils;

public class AppPreferences extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        openFragment(new SettingsFragment());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        updatePreferences(sharedPreferences, key);
                    }
                };
    }

    private void updatePreferences(SharedPreferences sp,
                                      String key) {
        switch(key) {
            case "pref_pushNoti" :
                ParseUtils.enableDisablePushNotification(sp.getBoolean(key, true));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFragment(android.app.Fragment fragment){
        android.app.FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.prefs_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

}
