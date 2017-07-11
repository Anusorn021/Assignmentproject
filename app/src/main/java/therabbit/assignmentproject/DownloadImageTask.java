package therabbit.assignmentproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nutherabbit on 11/7/2560.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ArrayList bmImage;
    MainActivity activity;

    public DownloadImageTask(MainActivity activity) {
        this.activity = activity;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        activity.getImg_data().add(result);
        activity.upDateList();


    }
}
