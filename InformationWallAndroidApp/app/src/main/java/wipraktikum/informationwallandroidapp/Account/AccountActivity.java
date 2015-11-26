package wipraktikum.informationwallandroidapp.Account;

/**
 * Created by Remi on 05.11.2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.R;

public class AccountActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(LoginManager.existPreviousAccountData()){
            openFragment(new AccountOverview(), false);
        }else{
            openFragment(new AccountLogIn(), false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            setTitle(getActionBarTitleByFragment(getActiveFragment()));
            return true;
        }else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            setTitle(getActionBarTitleByFragment(getActiveFragment()));
        }else {
            // Disable going back to the MainActivity
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getClass().getSimpleName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }



    public void openFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.account_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        if (addToBackStack)fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        //Change Title of Actionbar
        setTitle(getActionBarTitleByFragment(fragment));
    }

    private String getActionBarTitleByFragment(Fragment fragment) {
        if (fragment instanceof AccountLogIn){
            return getString(R.string.fragment_account_log_in_title);
        }else{
            return getString(R.string.fragment_account_overview_title);
        }
    }
}