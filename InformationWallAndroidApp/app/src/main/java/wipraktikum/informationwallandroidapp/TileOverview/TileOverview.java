package wipraktikum.informationwallandroidapp.TileOverview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.UploadManager;
import wipraktikum.informationwallandroidapp.TileOverview.Adapter.GridViewAdapter;


public class TileOverview extends BaseActivity {
    private InformationWallORMHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Adapter for GridView
        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(this));

        ArrayList<BlackBoardItem> blackBoardItems = DAOHelper.getInstance().getBlackBoardItemDAO().queryForAll();

        //JSON Test
        /*for(BlackBoardItem blackBoardItem : blackBoardItems) {
            JsonManager.getInstance().doAction(this, blackBoardItem);
        }*/
        //Upload Test
        //FileHelper.getInstance().showFileChooser(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int PICK_IMAGE_REQUEST = 1;

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap uploadImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                UploadManager.getInstance().uploadFile(uploadImage, "http://myinfowall.ddns.net/phpTest2.php");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
