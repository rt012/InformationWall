package wipraktikum.informationwallandroidapp.HttpConnection;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
            try {
                URL targetURL = new URL("http://lerain.tode.cz/s.php");
                HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
                //data send via POST request
                String data = "test=someData";
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "UTF-8");
                ((HttpURLConnection) conn).setFixedLengthStreamingMode(data.length());

                conn.setUseCaches(false);
                try (OutputStreamWriter out = new OutputStreamWriter(
                        conn.getOutputStream())) {
                    out.write(data);
                }

                //reading code after POST request (here I want to have value from "test" field
                try (BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()))) {
                    String currentLine;
                    while ((currentLine = in.readLine()) != null) {
                        System.out.println(currentLine);
                    }
                }
            }catch(Exception e){
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
