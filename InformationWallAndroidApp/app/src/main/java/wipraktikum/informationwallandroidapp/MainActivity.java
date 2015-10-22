package wipraktikum.informationwallandroidapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView)findViewById(R.id.gridview);

        try {
            insertTestData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        gridView.setAdapter(new GridViewAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Testing out the TodoOrmLiteExample app by creating some Todo entries in the database,
     * and querying for all the Todo object from the todo table.
     * @throws SQLException
     */
    private void insertTestData() throws SQLException {
        InformationWallORMHelper informationWallORMHelper = OpenHelperManager.getHelper(this,
                InformationWallORMHelper.class);
        Dao<Tile, Long> tileDao = informationWallORMHelper.getTileDAO();
        tileDao.createIfNotExists(new Tile(1, "Example Tile 1", R.drawable.slide_1, MainActivity.class.getName()));
        tileDao.createIfNotExists(new Tile(2, "Example Tile 2", R.drawable.slide_2, MainActivity.class.getName()));
        tileDao.createIfNotExists(new Tile(3, "Example Tile 3", R.drawable.slide_3, MainActivity.class.getName()));
    }
}
