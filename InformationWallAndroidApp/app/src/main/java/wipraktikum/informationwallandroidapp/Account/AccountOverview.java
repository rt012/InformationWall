package wipraktikum.informationwallandroidapp.Account;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

import wipraktikum.informationwallandroidapp.Account.Adapter.AccountOverviewListViewAdapter;
import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 22.11.2015.
 */
public class AccountOverview extends Fragment {
    private ListView accountOverviewListView = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_overview, viewGroup, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        accountOverviewListView = (ListView) view.findViewById(R.id.lv_account_overview);
        accountOverviewListView.setAdapter(new AccountOverviewListViewAdapter(getActivity(), 0, getPreviousLoggedInUsers()));
    }

    private List<User> getPreviousLoggedInUsers(){
        try {
            return DAOHelper.getInstance().getUserDAO().getPreviousLoggedInAccounts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
