package wipraktikum.informationwallandroidapp.ServerCommunication;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;

/**
 * Created by Remi on 29.10.2015.
 */
public class DownloadManager {

    private Context mContext;
    private static DownloadManager instance = null;

    private DownloadManager() {
        this.mContext = InfoWallApplication.getInstance();
    }

    public static DownloadManager getInstance(){
        if(instance == null){
            instance = new DownloadManager();
        }
        return instance;
    }

    public String downloadFile(String downloadUrl) {
        if (!FileHelper.getInstance().isURL(downloadUrl)) return null;
        final String fileName = FileHelper.getInstance().getFileName(downloadUrl);

        String file_url = downloadUrl;
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
        //Enqueue a new download and same the referenceId
        Long downloadReference = downloadManager.enqueue(request);

        return Environment.getExternalStorageDirectory() + "/Download/" + fileName;
    }



}
