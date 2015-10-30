package wipraktikum.informationwallandroidapp.BlackBoard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                openAddBlackBoardItemFragment();
            }
        });

        openBlackBoardOverview();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void openAddBlackBoardItemFragment(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        BlackBoardAddItem blackBoardAddItem = new BlackBoardAddItem();
        fragmentTransaction.replace(R.id.black_board_fragment_container, blackBoardAddItem);
        fragmentTransaction.commit();
    }

    public void openBlackBoardOverview(){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        BlackBoardOverview blackBoardOverview = new BlackBoardOverview();
        fragmentTransaction.replace(R.id.black_board_fragment_container, blackBoardOverview);
        fragmentTransaction.commit();
    }

}
