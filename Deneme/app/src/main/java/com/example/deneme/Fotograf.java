package com.example.deneme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fotograf extends AppCompatActivity {

    static String btnYazi;
    Intent intent;
    Bitmap imageBitmap;
    Uri imageUri;
    Button btnSecCek;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotograf);

        btnSecCek =(Button) findViewById(R.id.btnSecCek);
        Button btnoku = findViewById(R.id.btnOku);
        imageView = findViewById(R.id.imageView);

        if(btnYazi == "Galeri"){
            btnSecCek.setText("Galeriden Seç");
        }else{
            btnSecCek.setText("Kameradan Çek");
        }
    }

    public void SecCek(View view){

        if(btnYazi == "Galeri"){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }

        }else if(btnYazi == "Kamera"){

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }

            else{
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File image;
                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    image = createImageFile();
                    imageUri = Uri.fromFile(image);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, 3);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
        else if(requestCode == 2 && grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File asd;
            try {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                asd = createImageFile();
                imageUri = Uri.fromFile(asd);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivityForResult(intent, 3);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            try {
                imageUri = data.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode == 3 && resultCode == RESULT_OK){

            try{
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                imageView.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT) .show();
                Log.e("Camera", e.toString());
            }

        }

    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
