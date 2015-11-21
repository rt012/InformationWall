package wipraktikum.informationwallandroidapp.Utils;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Eric Schmidt on 21.11.2015.
 */
public class MediaStoreHelper {
    public static String getMediaStoreDataByUri(Uri uri){
        switch (getMediaType(uri)){
            case "audio":
                return MediaStore.Audio.Media.DATA;
            case "video":
                return MediaStore.Video.Media.DATA;
            default:
                return MediaStore.Images.Media.DATA;
        }
    }

    public static String getMediaStoreIDByUri(Uri uri){
        switch (getMediaType(uri)){
            case "audio":
                return MediaStore.Audio.Media._ID;
            case "video":
                return MediaStore.Video.Media._ID;
            default:
                return MediaStore.Images.Media._ID;
        }
    }

    public static Uri getMediaStoreContentUriByUri(Uri uri){
        switch (getMediaType(uri)){
            case "audio":
                return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            case "video":
                return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            default:
                return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
    }

    private static String getMediaType(Uri uri){
        //There are three cases (up till now): audio, video, otherwise (Documents and Images)
        String[] mediaType = uri.getPath().split("/"); //Get to the content type + ID
        mediaType = mediaType[mediaType.length-1].split(":"); //Get tot the content type
        return mediaType[0];
    }

}
