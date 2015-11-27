package wipraktikum.informationwallandroidapp.Account.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.List;

import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.ServerFilter;

/**
 * Created by Eric Schmidt on 27.11.2015.
 */
public class AccountAutoCompleteTextViewServerAdapter extends ArrayAdapter {
    private Context mContext = null;
    private List<String> mServerURLList = null;
    private ServerFilter mServerFilter = null;

    public AccountAutoCompleteTextViewServerAdapter(Context context, int resource, List<String> serverURL) {
        super(context, resource, serverURL);

        this.mServerURLList = serverURL;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView = view;
        String serverURL = (String) getItem(position);

        if (convertView == null){
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.server_item, viewGroup, false);
        }

        TextView tvServerURL = (TextView) convertView.
                findViewById(R.id.tv_account_log_in_adapter_server_url);
        tvServerURL.setText(mServerFilter.highlight(serverURL));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mServerFilter == null)
            mServerFilter = new ServerFilter(this, mServerURLList);

        return mServerFilter;
    }
}
