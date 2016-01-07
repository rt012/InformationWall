package wipraktikum.informationwallandroidapp.Feedreader.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.Feedreader.FeedReader;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 21.12.2015.
 */
public class FeedReaderListAdapter extends ArraySwipeAdapter {
    private Context context = null;
    private OnDeleteFeedListener mOnDeleteFeedListener = null;
    private boolean enableSwipe = true;

    public FeedReaderListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView = view;

        final Feed feed = (Feed) getItem(position);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.feed_item, null);

            if (enableSwipe != true) ((SwipeLayout) convertView).setSwipeEnabled(false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.feed_reader_icon);
        new LoadImageView(iconView).execute(feed.getImageURL());

        TextView tvTitle = (TextView) convertView.findViewById(R.id.feed_reader_list_title);
        tvTitle.setText(feed.getTitle());

        TextView tvWebsite = (TextView) convertView.findViewById(R.id.feed_reader_list_website);
        tvWebsite.setText(feed.getWebsite());

        TextView tcDesc = (TextView) convertView.findViewById(R.id.feed_reader_list_desc);
        tcDesc.setText(feed.getDescription());

        //Edit & Delete
        LinearLayout deleteFeed = (LinearLayout) convertView.findViewById(R.id.delete_object);
        deleteFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerOnDeleteFeedEvent(feed);
            }
        });

        LinearLayout editFeed = (LinearLayout) convertView.findViewById(R.id.edit_object);
        editFeed.setVisibility(View.GONE);

        //Add SwipeListener to the swipe layout
        addSwipeListener((SwipeLayout) convertView, convertView.findViewById(R.id.swipe), context);

        return convertView;
    }

    private void addSwipeListener(SwipeLayout swipeLayout, final View swipeView, final Context context){
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                if (isViewOverlapping(swipeView, ((FeedReader) context).getFab())){
                    ((FeedReader)context).showFab(false);
                }else{
                    ((FeedReader)context).showFab(true);
                }
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onClose(SwipeLayout layout) {
                ((FeedReader)context).showFab(true);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    private boolean isViewOverlapping(View firstView, View secondView) {
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        firstView.getLocationOnScreen(firstPosition);
        secondView.getLocationOnScreen(secondPosition);

        int r = firstView.getMeasuredWidth() + firstPosition[0];

        int firstHeight = firstView.getMeasuredHeight() + firstPosition[1];
        int secondHeight = secondPosition[1];

        return firstHeight >= secondHeight && (firstHeight != 0 && secondHeight != 0);

        // int l = secondPosition[0];
        // return r >= l && (r != 0 && l != 0);
    }


    public void setEnableSwipe(boolean enableSwipe){
        this.enableSwipe = enableSwipe;
    }

    private void triggerOnDeleteFeedEvent(Feed feed) {
        if (mOnDeleteFeedListener != null) {
            mOnDeleteFeedListener.onDeleteFeed(feed);
        }
    }

    public void setOnDeleteFeedListener(OnDeleteFeedListener onDeleteFeedListener){
        mOnDeleteFeedListener = onDeleteFeedListener;
    }

    public interface OnDeleteFeedListener {
        void onDeleteFeed(Feed feed);
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
