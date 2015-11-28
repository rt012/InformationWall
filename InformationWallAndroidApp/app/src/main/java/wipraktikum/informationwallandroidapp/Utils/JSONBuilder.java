package wipraktikum.informationwallandroidapp.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric Schmidt on 28.11.2015.
 */
public class JSONBuilder {
    private static final String KEY_FUNCTION = "function";
    private static final String KEY_VALUE = "value";

    public static JSONObject createJSONFromParam(String functionName, Object value){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_FUNCTION, functionName);
            jsonObject.put(KEY_VALUE, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject createJSONFromObject(Object object){
        JSONObject objectAsJsonObject = null;
        if(object != null) {
            try {
                Gson gsonHandler = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                String objectAsJsonString = gsonHandler.toJson(object);
                objectAsJsonObject = new JSONObject(objectAsJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objectAsJsonObject;
    }
}
