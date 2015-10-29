package wipraktikum.informationwallandroidapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;

/**
 * Created by Eric Schmidt on 29.10.2015.
 */
public class FileHelper {
    private static FileHelper instance = null;
    private Context context;

    private FileHelper(Context context){
        this.context = context;
    }

    public static FileHelper getInstance(Context context){
        if (instance == null){
            instance = new FileHelper(context);
        }
        return instance;
    }

    public void openFile(String fullFileName, DBBlackBoardAttachment.DataType dataType) {
        File file = new File(fullFileName);
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), getDataTyp(dataType));
        context.startActivity(i);
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
