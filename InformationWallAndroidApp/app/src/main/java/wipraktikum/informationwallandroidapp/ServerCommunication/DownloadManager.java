package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;

/**
 * Created by Remi on 29.10.2015.
 */
public class DownloadManager {

    Context mContext;

    public DownloadManager(Context context) {
        this.mContext = context;
    }


    public void downloadFile(final BlackBoardAttachment attachment) {

        File attachmentFile = new File(attachment.getRemoteDataPath());
        final String fileName = attachmentFile.getName();

        String file_url = attachment.getRemoteDataPath();
        android.app.DownloadManager downloadManager = (android.app.DownloadManager)mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(file_url);
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI | android.app.DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications.
        request.setTitle(fileName);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,fileName);
        //Enqueue a new download and same the referenceId
        Long downloadReference = downloadManager.enqueue(request);
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                openFile(fileName, attachment);
            }
        }, new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //new DownloadFileFromURL(context).execute(file_url);
    }

    private void openFile(String fileName, BlackBoardAttachment attachment) {
        File file = new File(Environment.DIRECTORY_DOWNLOADS+fileName);
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), getDataTyp(attachment.getDataType()));
        mContext.startActivity(i);
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


}
