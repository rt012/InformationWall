package wipraktikum.informationwallandroidapp.Utils;

/**
 * Created by Eric Schmidt on 01.11.2015.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

import wipraktikum.informationwallandroidapp.InfoWallApplication;

public class RealPathHelper{
    private static RealPathHelper instance = null;
    private Context mContext = null;

    private RealPathHelper(){
        mContext = InfoWallApplication.getInstance();
    }

    public static RealPathHelper getInstance(){
        if (instance == null){
            instance = new RealPathHelper();
        }
        return instance;
    }

    public String getRealPathFromURI(Uri data){
        String realPath;

        File file = new File(data.getPath());
        if (FileHelper.getInstance().exists(file.getAbsolutePath())){
            realPath = file.getAbsolutePath();
        }else {
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = getRealPathFromURI_BelowAPI11(data);

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = getRealPathFromURI_API11to18(data);

                // SDK > 19 (Android 4.4)
            else
                realPath = getRealPathFromURI_API19(data);
        }
        return realPath;
    }

    @SuppressLint("NewApi")
    private String getRealPathFromURI_API19(Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @SuppressLint("NewApi")
    private String getRealPathFromURI_API11to18(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    private String getRealPathFromURI_BelowAPI11(Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}