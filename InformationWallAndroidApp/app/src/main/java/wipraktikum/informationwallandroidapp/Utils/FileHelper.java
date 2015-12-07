package wipraktikum.informationwallandroidapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import java.io.File;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 29.10.2015.
 */
public class FileHelper {
    public static int PICK_ATTACHMENT_REQUEST = 1;
    private final String YOUTUBE_URL = "www.youtube.com";

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

    public void openFile(Context activity, String fullFileName) {
        File file = new File(fullFileName);
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        if (!FileHelper.getInstance().isURL(fullFileName)) {
            intent.setDataAndType(Uri.fromFile(file),
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension
                            (FileHelper.getInstance().getFileExtension(file.getAbsolutePath().toLowerCase())));
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }else{
            intent.setData(Uri.parse(fullFileName));
        }
        activity.startActivity(intent);
    }

    public void showFileChooser(Activity activity) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Attachment"), PICK_ATTACHMENT_REQUEST);
    }

    public String getFileName(String fullFileName){
        File attachmentFile = new File(fullFileName);
        return attachmentFile.getName();
    }

    public String getFileExtension(String fullFileName){
        String filenameArray[] = fullFileName.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        return extension;
    }

    public DBBlackBoardAttachment.DataType getBlackBoardAttachmentDataType(String fullFileName){
        String extension = getFileExtension(fullFileName);

        switch (extension) {
            case "PDF":
            case "pdf":
                return DBBlackBoardAttachment.DataType.PDF;
            case "png":
            case "jpg":
            case "jpeg":
            case "bmp":
                return DBBlackBoardAttachment.DataType.IMG;
        }
        //Check if fileName is a URL and act accordingly
        if (fullFileName.toLowerCase().contains(YOUTUBE_URL)){
            return DBBlackBoardAttachment.DataType.YOUTUBE;
        }

        return DBBlackBoardAttachment.DataType.OTHER;
    }

    public boolean isURL(String fullFileName){
        return URLUtil.isValidUrl(fullFileName);
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
