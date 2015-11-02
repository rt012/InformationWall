package wipraktikum.informationwallandroidapp.Utils;

/**
 * Created by Eric Schmidt on 01.11.2015.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
            //There are three cases (up till now): audio, video, otherwise (Documents and Images)
            String[] mediaType = data.getPath().split("/"); //Get to the content type + ID
            mediaType = mediaType[mediaType.length-1].split(":"); //Get tot the content type
            switch (mediaType[0]){
                case "audio":
                    realPath = getRealPathFromURIAudio(data);
                    break;
                case "video":
                    realPath = getRealPathFromURIVideo(data);
                    break;
                default:
                    realPath = getRealPathFromURIDocument(data);
                    break;
            }
        }
        return realPath;
    }

    @SuppressLint("NewApi")
    private String getRealPathFromURIDocument(Uri uri){
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
    private String getRealPathFromURIVideo(Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Video.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Video.Media._ID + "=?";

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    private String getRealPathFromURIAudio(Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Audio.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Audio.Media._ID + "=?";

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}