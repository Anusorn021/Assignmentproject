package therabbit.assignmentproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Nutherabbit on 10/7/2560.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{


    Context context;
    MainActivity activity;
    ArrayList<ImgData> data;
    public MyRecyclerViewAdapter(MainActivity activity, ArrayList<ImgData> data) {

        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.data = data;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.low_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (data != null){

            if (data.get(position).getType().equals("local")){
                Log.d("zzz",data.get(position).getImg_path());
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.imageView.setImageBitmap(data.get(position).getBitmap());
                viewHolder.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog diaBox = AskOption(position);
                        diaBox.show();
                    }
                });
            }
            else if (data.get(position).getType().equals("server")){
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.imageView.setImageBitmap(data.get(position).getBitmap());

            }

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button del;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            del = (Button) itemView.findViewById(R.id.delete);
        }
    }
    private void removeItem(int position) {

        activity.deleteImgLocal(position);

        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());

    }
    private AlertDialog AskOption(final int position)
    {
        AlertDialog alertDialog =new AlertDialog.Builder(activity)
                .setTitle("Delete")
                .setMessage("Do you want to Delete")


                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeItem(position);

                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return alertDialog;

    }
    public Bitmap getBitmapFormPath(String photoPath){
//        File sd = Environment.getExternalStorageDirectory();
//        File image = new File(sd+filePath, imageName);
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
//        bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        return bitmap;
    }


}
