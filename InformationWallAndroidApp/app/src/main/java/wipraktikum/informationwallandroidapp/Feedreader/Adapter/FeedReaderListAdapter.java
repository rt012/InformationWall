package wipraktikum.informationwallandroidapp.Feedreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 21.12.2015.
 */
public class FeedReaderListAdapter extends ArrayAdapter {
    Context context = null;

    public FeedReaderListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView = view;
        Feed feed = (Feed) getItem(position);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.feed_item, null);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.feed_reader_list_title);
        tvTitle.setText(feed.getTitle());

        TextView tvWebsite = (TextView) convertView.findViewById(R.id.feed_reader_list_website);
        tvWebsite.setText(feed.getWebsite());

        TextView tcDesc = (TextView) convertView.findViewById(R.id.feed_reader_list_desc);
        tcDesc.setText(feed.getDescription());

        return convertView;
    }
}
