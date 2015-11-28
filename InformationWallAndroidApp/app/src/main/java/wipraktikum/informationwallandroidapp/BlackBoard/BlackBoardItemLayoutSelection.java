package wipraktikum.informationwallandroidapp.BlackBoard;

/**
 * Created by Remi on 26.11.2015.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.R;


public class BlackBoardItemLayoutSelection extends BaseActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager mPager;

    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;

    private Button saveButton;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStatePagerAdapter mPagerAdapter;
    private TextView tvLayoutDesc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_board_layout_selection);

        initViews();
        setToolbar();
        setPagerAdapter();
        setUiPageViewController();
    }

    private void initViews(){
        tvLayoutDesc = (TextView) findViewById(R.id.tv_layout_name);
        saveButton = (Button) findViewById(R.id.btn_save_layout);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedLayout();
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Select Layout");
    }

    private void setPagerAdapter() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setDescriptionTextView(position);
                mPagerAdapter.getItem(position);
                invalidateOptionsMenu();
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
                dots[position].setTextColor(getResources().getColor(R.color.ci_color));
            }
        });
    }

    private void setUiPageViewController() {
        dotsLayout = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        dotsCount = mPagerAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }

        dots[0].setTextColor(getResources().getColor(R.color.ci_color));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_black_board_add_item, menu);
        return true;
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
        int currentIndex = mPager.getCurrentItem();
        // Save the layout index somewhere
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return LayoutSelectionFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void setDescriptionTextView(int position){
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
}

