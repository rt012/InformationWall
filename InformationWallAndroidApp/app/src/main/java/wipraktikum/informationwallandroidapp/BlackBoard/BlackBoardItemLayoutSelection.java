package wipraktikum.informationwallandroidapp.BlackBoard;

/**
 * Created by Remi on 26.11.2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.NotificationHelper;


public class BlackBoardItemLayoutSelection extends Fragment {
    private static final int NUM_PAGES = 3;

    private ViewPager mPager;

    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private OnLayoutSelectListener mOnLayoutSelectListener = null;

    private BlackBoardItem mCurrentBlackBoardItem;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStatePagerAdapter mPagerAdapter;
    private TextView tvLayoutDesc = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blackboard_layout_selection, viewGroup, false);
        setHasOptionsMenu(true);

        setRetainInstance(true);
        initViews(view);
        setToolbar(view);
        setPagerAdapter(view);
        setUiPageViewController(view);

        setInitialState();
        initNewBlackBoardItemNotificationListener();

        return view;
    }

    private void initNewBlackBoardItemNotificationListener() {
        //Register Listener
        NotificationHelper.getInstance().setOnNotificationReceiveListener(new NotificationHelper.OnNotificationReceiveListener() {
            @Override
            public void onNotificationReceive() {
                NotificationHelper.showNewBlackBoardItemSnackbar(((Blackboard) getActivity()).getRootView(), getString(R.string.new_blackboard_item_notification_title));

            }
        });
    }

    private void initViews(View view){
        tvLayoutDesc = (TextView) view.findViewById(R.id.tv_layout_name);
    }

    private void setToolbar(View view) {
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle("Select Layout");
    }

    private void setPagerAdapter(View view) {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setDescriptionTextViewByPosition(position);
                mPagerAdapter.getItem(position);
                getActivity().invalidateOptionsMenu();
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
                dots[position].setTextColor(getResources().getColor(R.color.ci_color));
            }
        });
    }

    private void setUiPageViewController(View view) {
        dotsLayout = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        dotsCount = mPagerAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }

        dots[0].setTextColor(getResources().getColor(R.color.ci_color));
    }

    @Override
    public void onResume() {
        super.onResume();

        setInitialState();
    }

    private void setInitialState() {
        if(getArguments().containsKey("currentBlackBoardItem")) {
            mCurrentBlackBoardItem = new Gson().fromJson(getArguments().getString("currentBlackBoardItem"), BlackBoardItem.class);
            if(mCurrentBlackBoardItem.getLayoutType() != null) {
                mPager.setCurrentItem(mCurrentBlackBoardItem.getLayoutType().ordinal(), true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_black_board_add_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_black_board_item_save) {
                saveSelectedLayout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSelectedLayout() {
        if (mOnLayoutSelectListener != null) {
            mOnLayoutSelectListener.onLayoutSelect(getLayoutTypeByPosition(mPager.getCurrentItem()));
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            LayoutSelectionFragment layoutSelectionFragment = LayoutSelectionFragment.create(position);
            layoutSelectionFragment.setOnLayoutSelectListener(new LayoutSelectionFragment.OnLayoutSelectListener() {
                @Override
                public void onLayoutSelect(int pageNumber) {
                    saveSelectedLayout();
                }
            });
            return layoutSelectionFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void setDescriptionTextViewByPosition(int position){
        switch (position){
            case 0:
                tvLayoutDesc.setText(R.string.blackboard_layout_text_only);
                break;
            case 1:
                tvLayoutDesc.setText(R.string.blackboard_layout_document);
                break;
            case 2:
                tvLayoutDesc.setText(R.string.blackboard_layout_document_and_info);
                break;
        }
    }

    private DBBlackBoardItem.LayoutType getLayoutTypeByPosition(int position){
        switch (position){
            case 0:
                return DBBlackBoardItem.LayoutType.TEXT_ONLY;
            case 1:
                return DBBlackBoardItem.LayoutType.ATTACHMENT_ONLY;
            case 2:
                return DBBlackBoardItem.LayoutType.TEXT_AND_ATTACHMENT;
        }

        return DBBlackBoardItem.LayoutType.TEXT_ONLY;
    }

    public void setOnLayoutSelectListener(OnLayoutSelectListener onLayoutSelectListener){
        mOnLayoutSelectListener = onLayoutSelectListener;
    }

    public interface OnLayoutSelectListener{
        void onLayoutSelect(DBBlackBoardItem.LayoutType layoutType);
    }
}

