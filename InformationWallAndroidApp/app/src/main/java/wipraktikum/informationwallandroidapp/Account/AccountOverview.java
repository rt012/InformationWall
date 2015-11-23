package wipraktikum.informationwallandroidapp.Account;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

import wipraktikum.informationwallandroidapp.Account.Adapter.AccountOverviewListViewAdapter;
import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.TileOverview.TileOverview;

/**
 * Created by Eric Schmidt on 22.11.2015.
 */
public class AccountOverview extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_overview, viewGroup, false);
        initViews(view);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void initViews(View view) {
        final ListView accountOverviewListView = (ListView) view.findViewById(R.id.lv_account_overview);
        accountOverviewListView.setAdapter(new AccountOverviewListViewAdapter(getActivity(), 0, getPreviousLoggedInUsers()));
        accountOverviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) accountOverviewListView.getItemAtPosition(position);
                handleNavigationByUser(user);
            }
        });
        final Button newAccount = (Button) view.findViewById(R.id.bt_new_account);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountActivity) getActivity()).openFragment(createLogInFragment(null), true);
            }
        });
    }

    private List<User> getPreviousLoggedInUsers(){
        try {
            return DAOHelper.getInstance().getUserDAO().getPreviousLoggedInAccounts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleNavigationByUser(User user){
        if (user.isKeepLogInData()){
            LogInManager.logInUser(user);
            //openTileOverview();
            getActivity().finish();
        }else{
            ((AccountActivity) getActivity()).openFragment(createLogInFragment(user), true);
        }
    }

    private void openTileOverview() {
        Intent intent = new Intent(getActivity(), TileOverview.class);
        startActivity(intent);
    }

    private Fragment createLogInFragment(User user){
        AccountLogIn fragment = new AccountLogIn();
        if (user != null) fragment.setArguments(createLogInBundle(user));
        return fragment;
    }

    private Bundle createLogInBundle(User user){
        Bundle params = new Bundle();
        params.putLong(AccountLogIn.USER_ID_TAG, user.getUserID());
        return params;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_black_board_add_item, menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_logout).setEnabled(false);
    }

}
