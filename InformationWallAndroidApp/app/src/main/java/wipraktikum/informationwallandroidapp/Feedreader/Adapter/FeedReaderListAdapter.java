package wipraktikum.informationwallandroidapp.Feedreader.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
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

        ImageView iconView = (ImageView) convertView.findViewById(R.id.feed_reader_icon);
        new LoadImageView(iconView).execute(feed.getImageURL());

        TextView tvTitle = (TextView) convertView.findViewById(R.id.feed_reader_list_title);
        tvTitle.setText(feed.getTitle());

        TextView tvWebsite = (TextView) convertView.findViewById(R.id.feed_reader_list_website);
        tvWebsite.setText(feed.getWebsite());

        TextView tcDesc = (TextView) convertView.findViewById(R.id.feed_reader_list_desc);
        tcDesc.setText(feed.getDescription());

        return convertView;
    }

    private class LoadImageView extends AsyncTask<String, Void, Drawable> {

        private ImageView mIconView = null;

        public LoadImageView(ImageView iconView){
            mIconView = iconView;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            Drawable drawable = null;
            try {
                URL url = new URL(params[0]);
                InputStream content = (InputStream) url.getContent();
                drawable = Drawable.createFromStream(content , "src");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            mIconView.setImageDrawable(result);
        }

        @Override
        protected void onPreExecute() {
            mIconView.setImageResource(R.drawable.ic_upload);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
