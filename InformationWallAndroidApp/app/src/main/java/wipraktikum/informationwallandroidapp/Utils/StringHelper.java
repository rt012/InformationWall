package wipraktikum.informationwallandroidapp.Utils;

/**
 * Created by Eric Schmidt on 20.11.2015.
 */
public class StringHelper {

    public static boolean isStringNullOrEmpty(String string){
        if (string != null) {
            string = string.trim();
            if (string.matches("")) {
                return true;
            }
            return false;
        }else{
            return true;
        }
    }
}
