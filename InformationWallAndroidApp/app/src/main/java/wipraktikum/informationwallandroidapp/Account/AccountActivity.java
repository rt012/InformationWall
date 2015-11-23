package wipraktikum.informationwallandroidapp.Account;

/**
 * Created by Remi on 05.11.2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

public class AccountActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(existPreviousAccountData()){
            openFragment(new AccountOverview(), false);
        }else{
            openFragment(new AccountLogIn(), false);
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

    private Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getClass().getSimpleName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private boolean existPreviousAccountData(){
        try {
            if (!DAOHelper.getInstance().getUserDAO().getPreviousLoggedInAccounts().isEmpty()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
        if (fragment instanceof AccountOverview){
            return getString(R.string.fragment_account_overview_title);
        }else{
            return getString(R.string.fragment_account_log_in_title);
        }
    }
}