package therabbit.assignmentproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nutherabbit on 11/7/2560.
 */

public class AsyncGetData extends AsyncTask<String, String, String> {
    ArrayList<String> urlList = new ArrayList<>();
    ProgressDialog pdLoading;
    HttpURLConnection conn;
    URL url = null;
    MainActivity activity;

    public AsyncGetData(MainActivity activity) {
        this.activity = activity;
        pdLoading = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";
        try {

            url = new URL("https://jsonplaceholder.typicode.com/albums/1/photos");


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
        try {

            // Setup HttpURLConnection class to send and receive data from php and mysql
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream iStream = httpURLConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();


            br.close();
            return data;

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }



    }

    @Override
    protected void onPostExecute(String s) {
        pdLoading.dismiss();


        try {

            JSONArray jArray = new JSONArray(s);


            for (int i = 0; i < jArray.length(); i++) {

                if ((i % 2) == 0){
                    urlList.add(jArray.getJSONObject(i).get("thumbnailUrl").toString());
                    ImgData imgData = new ImgData();
                    Random random = new Random();
                    imgData.setImd_id(random.nextInt());
                    imgData.setImg_path(jArray.getJSONObject(i).get("thumbnailUrl").toString());
                    imgData.setType("server");
                    activity.insertImg(imgData);
                    new DownloadImageTask(activity,imgData).execute(jArray.getJSONObject(i).get("thumbnailUrl").toString());
                    activity.getDatas().add(imgData);
                }
                else {
                    urlList.add(jArray.getJSONObject(i).get("url").toString());
                    ImgData imgData = new ImgData();
                    Random random = new Random();
                    imgData.setImd_id(random.nextInt());
                    imgData.setImg_path(jArray.getJSONObject(i).get("url").toString());
                    imgData.setType("server");

                    new DownloadImageTask(activity,imgData).execute(jArray.getJSONObject(i).get("url").toString());
                    activity.getDatas().add(imgData);
                    activity.insertImg(imgData);
                }
            }
            /*for (int i = 0; i < urlList.size(); i++) {
                new DownloadImageTask(activity).execute(urlList.get(i));

            }*/


        } catch (JSONException e) {
            System.out.println(e.toString());

        }
    }
}
