package wipraktikum.informationwallandroidapp.BlackBoard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoard extends AppCompatActivity {

    private MenuItem saveItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddBlackBoardItemFragment();
                fab.hide();
                saveItem.setVisible(true);
            }
        });

        openBlackBoardOverview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_black_board, menu);

        saveItem = menu.findItem(R.id.menu_black_board_item_save);
        saveItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void openAddBlackBoardItemFragment(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        BlackBoardAddItem blackBoardAddItem = BlackBoardAddItem.getInstance();
        fragmentTransaction.replace(R.id.black_board_fragment_container, blackBoardAddItem);
        fragmentTransaction.commit();
    }

    public void openBlackBoardOverview(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        BlackBoardOverview blackBoardOverview = BlackBoardOverview.getInstance();
        fragmentTransaction.replace(R.id.black_board_fragment_container, blackBoardOverview);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
