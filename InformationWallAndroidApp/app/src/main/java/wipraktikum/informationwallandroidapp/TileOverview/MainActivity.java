package wipraktikum.informationwallandroidapp.TileOverview;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoard;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.ContactAdress;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
import wipraktikum.informationwallandroidapp.HttpConnection.HttpConnectionPhp;
import wipraktikum.informationwallandroidapp.R;


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

        HttpConnectionPhp test = new HttpConnectionPhp();
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
        tileDao.createIfNotExists(new Tile(1, "Example Tile 1", R.drawable.slide_1, BlackBoard.class.getName()));
        tileDao.createIfNotExists(new Tile(2, "Example Tile 2", R.drawable.slide_2, MainActivity.class.getName()));
        tileDao.createIfNotExists(new Tile(3, "Example Tile 3", R.drawable.slide_3, MainActivity.class.getName()));

        Dao<BlackBoardItem, Long> blackBoardItemsDAO = informationWallORMHelper.getBlackBoardItemDAO();
        Collection<BlackBoardAttachment> attachmentList = new ArrayList<BlackBoardAttachment>();
        attachmentList.add(new BlackBoardAttachment(1, "http://localhost/imagestore/test.pdf", "C://temp/test/test.pdf", "PDF"));
        attachmentList.add(new BlackBoardAttachment(2, "http://localhost/imagestore/test.pdf", "C://temp/test/test.pdf", "PDF"));
        attachmentList.add(new BlackBoardAttachment(3, "http://localhost/imagestore/test.pdf", "C://temp/test/test.pdf", "PDF"));
        attachmentList.add(new BlackBoardAttachment(4, "http://localhost/imagestore/test.pdf", "C://temp/test/test.pdf", "PDF"));



        Contact dummyContact = new Contact(1, "Max", "Mustermann", "maxMustermann@test.de", "234242342345", "Wunschfirma XY", new ContactAdress(0, "Teststraße", "23", "242342", "Stuttgart"));
        Dao<Contact, Long> contactDAO = informationWallORMHelper.getContactDAO();
        contactDAO.createIfNotExists(dummyContact);

        BlackBoardItem item1 = new BlackBoardItem(1, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", null, new Date(), new Date());
        item1.setmBlackBoardAttachment(attachmentList);
        blackBoardItemsDAO.createIfNotExists(item1);

        BlackBoardItem item2 = new BlackBoardItem(2, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", null, new Date(), new Date());
        item2.setmBlackBoardAttachment(attachmentList);
        blackBoardItemsDAO.createIfNotExists(item2);

        BlackBoardItem item3 = new BlackBoardItem(3, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", null, new Date(), new Date());
        item3.setmBlackBoardAttachment(attachmentList);
        blackBoardItemsDAO.createIfNotExists(item3);

        BlackBoardItem item4 = new BlackBoardItem(4, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", null, new Date(), new Date());
        item4.setmBlackBoardAttachment(attachmentList);
        blackBoardItemsDAO.createIfNotExists(item4);
        /*blackBoardItemsDAO.createIfNotExists(new BlackBoardItem(1, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", attachmentList, new Date(), new Date()));
        blackBoardItemsDAO.createIfNotExists(new BlackBoardItem(2, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", attachmentList, new Date(), new Date()));
        blackBoardItemsDAO.createIfNotExists(new BlackBoardItem(3, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", attachmentList, new Date(), new Date()));
        blackBoardItemsDAO.createIfNotExists(new BlackBoardItem(4, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", attachmentList, new Date(), new Date()));
        blackBoardItemsDAO.createIfNotExists(new BlackBoardItem(5, "test1", dummyContact, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", attachmentList, new Date(), new Date()));
       */
       /* List<BlackBoardItem> items = blackBoardItemsDAO.queryForAll();
        for(int i = 0; i < blackBoardItemsDAO.queryForAll().size(); i++) {
            items.get(i).setmBlackBoardAttachment(attachmentList);
        }*/
    }
}
