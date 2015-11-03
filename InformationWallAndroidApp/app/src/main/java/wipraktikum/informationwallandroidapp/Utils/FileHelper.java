package wipraktikum.informationwallandroidapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 29.10.2015.
 */
public class FileHelper {
    public static int PICK_ATTACHMENT_REQUEST = 1;

    private static FileHelper instance = null;
    private Context mContext;

    private FileHelper(){
        mContext = InfoWallApplication.getInstance();
    }

    public static FileHelper getInstance(){
        if (instance == null){
            instance = new FileHelper();
        }
        return instance;
    }

    public void openFile(Context activity, String fullFileName, DBBlackBoardAttachment.DataType dataType) {
        File file = new File(fullFileName);
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), getDataTyp(dataType));
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activity.startActivity(intent);
    }

    public void showPictureChooser(Activity activity) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Attachment"), PICK_ATTACHMENT_REQUEST);
    }

    private String getDataTyp(DBBlackBoardAttachment.DataType dataType) {
        String fileType;
        switch (dataType) {
            case PDF:
                fileType = "application/pdf";
                break;
            case IMG:
                fileType = "image/*";
                break;
            default:
                fileType = null;
                break;
        }
        return fileType;
    }

    public String getFileName(String fullFileName){
        File attachmentFile = new File(fullFileName);
        return attachmentFile.getName();
    }

    public boolean exists(String fullFileName){
        boolean exist = false;
        if (fullFileName != null) {
            File file = new File(fullFileName);

            if (file.exists()) {
                exist = true;
            }
        }
        return exist;
    }
}