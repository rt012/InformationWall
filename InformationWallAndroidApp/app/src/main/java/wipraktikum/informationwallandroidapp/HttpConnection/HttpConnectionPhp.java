package wipraktikum.informationwallandroidapp.HttpConnection;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Eric Schmidt on 22.10.2015.
 */
public class HttpConnectionPhp
{
    public HttpConnectionPhp() {
        new DownloadFilesTask().execute();
    }

    private class DownloadFilesTask extends AsyncTask<Void, Integer, Long> {
        @Override
        protected Long doInBackground(Void... voids) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.0.17/test.php");

            try {
                // Add your data
                JSONObject obj = new JSONObject();
                obj.put("name", "mkyong.com");
                httppost.addHeader("content-type", "application/x-www-form-urlencoded");

                StringEntity requestEntity = new StringEntity(
                        "json="+obj.toString());
                httppost.setEntity(requestEntity);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }
}
