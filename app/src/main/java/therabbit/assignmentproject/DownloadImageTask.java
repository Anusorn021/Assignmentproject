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
    MainActivity activity = null;
    MyRecyclerViewAdapter.ViewHolder viewHolder = null;
    ImgData imgData = null;

    public DownloadImageTask(MainActivity activity) {
        this.activity = activity;
    }

    public DownloadImageTask(MyRecyclerViewAdapter.ViewHolder viewHolder,MainActivity activity) {
        this.viewHolder = viewHolder;
        this.activity = null;
    }

    public DownloadImageTask(MainActivity activity ,ImgData imgData) {
        this.imgData = imgData;
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
        if (activity != null ){
            activity.getImg_data().add(result);
            activity.upDateList();
        }
        else if (viewHolder != null){
            viewHolder.imageView.setImageBitmap(result);

        }
        if (imgData != null){
            imgData.setBitmap(result);
            activity.upDateList();
        }


    }
}
