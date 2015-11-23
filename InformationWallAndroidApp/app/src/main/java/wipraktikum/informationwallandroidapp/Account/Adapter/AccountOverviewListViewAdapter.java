package wipraktikum.informationwallandroidapp.Account.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 22.11.2015.
 */
public class AccountOverviewListViewAdapter extends ArrayAdapter {

    private final Context context;
    private List<User> mLoggedInUsers = new ArrayList<User>();

    public AccountOverviewListViewAdapter(Context context, int textViewResourceId,
        List<User> loggedInUsers) {
        super(context, textViewResourceId, loggedInUsers);
        this.mLoggedInUsers = loggedInUsers;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User) getItem(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.account_item, parent, false);

        TextView tvFirstChar = (TextView) view.findViewById(R.id.tv_account_overview_first_char);
        String firstChar = user.getEmailAddress().substring(0, 1);
        tvFirstChar.setText(firstChar.toUpperCase());

        TextView tvEmail = (TextView) view.findViewById(R.id.tv_account_overview_email);
        tvEmail.setText(user.getEmailAddress());

        TextView tvServer = (TextView) view.findViewById(R.id.tv_account_overview_server);
        tvServer.setText(user.getServerURL());

        return view;
    }


}
