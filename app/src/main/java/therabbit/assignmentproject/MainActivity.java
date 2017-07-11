package therabbit.assignmentproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public RecyclerView recyclerView;

    public RecyclerView.LayoutManager layoutManager;
    public Button addImg;
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public String choose;

    public MyRecyclerViewAdapter viewAdapter;
    private Realm realm = null;
    public ArrayList<String> urlList = new ArrayList<>();
    public ArrayList<ImgData> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addImg = (Button) findViewById(R.id.addImg);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        addImg.setOnClickListener(this);

        realm = Realm.getDefaultInstance();


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        viewAdapter = new MyRecyclerViewAdapter(this,datas);
        recyclerView.setAdapter(viewAdapter);

        RealmResults<RealmImageObject> xx = realm.where(RealmImageObject.class).findAll();

        if (xx == null) {
            System.out.println("SSSSSSSSSSSS");
            new AsyncGetData(this).execute();
        } else {
            System.out.println("FFFFFFFFF");
            showObject();
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //realm.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        //realm.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (choose.equals("Take Image"))
                        cameraIntent();
                    else if (choose.equals("Choose from Library"))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addImg) {
            selectImage();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Image", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Take Image")) {
                    choose = "Take Image";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    choose = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //img_data.add(thumbnail);
        ImgData imgData = new ImgData();
        imgData.setImg_path(destination.getPath());
        Random random = new Random();
        imgData.setImd_id(random.nextInt());
        imgData.setType("local");
        imgData.setBitmap(thumbnail);
        datas.add(imgData);
        insertImg(imgData);
        upDateList();


        //ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //img_data.add(bm);


        Bitmap bmp = bm;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        ImgData imgData = new ImgData();



        Uri selectedImage = data.getData();
        imgData.setImg_path(getRealPathFromURI(selectedImage));
        Random random = new Random();
        imgData.setImd_id(random.nextInt());
        imgData.setType("local");
        imgData.setBitmap(bm);
        datas.add(imgData);
        insertImg(imgData);
        upDateList();
        //ivImage.setImageBitmap(bm);
    }

    public void insertImg(final ImgData data) {
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                            RealmImageObject realmImageObject = realm.createObject(RealmImageObject.class);
                            realmImageObject.setImd_id(data.getImd_id());
                            realmImageObject.setType(data.getType());
                            realmImageObject.setImg_path(data.getImg_path());
                            Bitmap bitmap = data.getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            realmImageObject.setbitByte(byteArray);

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(getApplicationContext(), "save complete.", Toast.LENGTH_SHORT).show();

                    }

                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = managedQuery( contentUri, proj, null, null,null);

        if (cursor == null) return null;

        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }



    public void upDateList() {
        viewAdapter.notifyDataSetChanged();
        for (int i = 0; i < datas.size(); i++) {
            Log.d("ddd",datas.get(i).getImd_id()+" "+datas.get(i).getImg_path());

        }

    }

    public void deleteImgLocal(int position) {
        //img_data.remove(position);
        Log.d("1 ",position+"");
        //Log.d("2 ",img_data.size()+"");
        Log.d("3 ",datas.get(position).getImg_path()+"");
        File file = new File(datas.get(position).getImg_path()+"");
        file.delete();
        removeRealmObject(position);
        for (int i = 0; i < datas.size(); i++) {
            Log.d("delete",datas.get(i).getImd_id()+" "+datas.get(i).getImg_path());

        }



    }

    public void removeRealmObject(final int position){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmImageObject> rows = realm.where(RealmImageObject.class).findAll();
                for (int i = 0; i < rows.size(); i++) {
                    if (position == i){
                        rows.deleteFromRealm(position);
                    }
                }
            }
        });
    }

    public ArrayList<ImgData> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<ImgData> datas) {
        this.datas = datas;
    }
    public void showObject(){
        RealmResults<RealmImageObject> objects = realm.where(RealmImageObject.class).findAll();
        Log.d("re ",objects.size()+"");
        for (int i = 0; i < objects.size(); i++) {
            Log.d("realm",objects.get(i).getImg_path());
            ImgData imgData = new ImgData();
            imgData.setImg_path(objects.get(i).getImg_path());
            imgData.setImd_id(objects.get(i).getImd_id());
            imgData.setType(objects.get(i).getType());
            Bitmap bitmap = BitmapFactory.decodeByteArray(objects.get(i).getbitByte(), 0, objects.get(i).getbitByte().length);
            imgData.setBitmap(bitmap);
            datas.add(imgData);

        }
        upDateList();
    }
}
