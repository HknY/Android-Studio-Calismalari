package com.example.deneme;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Fotograf extends AppCompatActivity {

    static String btnYazi;
    static String yazi = "";
    static boolean enabled = false;
    Intent intent;
    Bitmap imageBitmap;
    Uri imageUri;
    Button btnSecCek;
    Button btnoku;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotograf);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnSecCek = (Button) findViewById(R.id.btnSecCek);
        btnoku = findViewById(R.id.btnOku);
        btnoku.setEnabled(enabled);
        imageView = findViewById(R.id.imageView);

        if (btnYazi == "Galeri") {
            btnSecCek.setText("Galeriden Seç");
            imageView.setImageResource(R.drawable.ic_image_black_24dp);
        } else {
            btnSecCek.setText("Kameradan Çek");
            imageView.setImageResource(R.drawable.ic_photo_camera_black_24dp);
        }

    }

    public void Oku(View view) {

        String okunan = "";
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        Frame imageFrame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            String[] satirlar = textBlocks.valueAt(i).getValue().split("\n");
            for (String satir : satirlar) {
                String[] kelimeler = satir.split(" ");
                for (String kelime : kelimeler) {
                    for (int j = 0; j < MainActivity.veritabani.size(); j++) {
                        if (kelime.equals(MainActivity.veritabani.keyAt(j))) {
                            okunan += kelime + ", ";
                        }
                    }
                }
            }
        }
        yazi = okunan;
        MainActivity.textView.setText(MainActivity.textView.getText() + okunan);

        String[] yazi = MainActivity.textView.getText().toString().split(", ");

        if (yazi[0].split(": ").length < 2) {
            MainActivity.txt_bilgi.setVisibility(View.INVISIBLE);
            MainActivity.bar.setProgress(0);
            MainActivity.txt_sonuc.setText("Sonuç: ");
        } else
            MainActivity.txt_bilgi.setVisibility(View.VISIBLE);

        finish();
    }

    public void SecCek(View view) {

        if (btnYazi == "Galeri") {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }

        } else if (btnYazi == "Kamera") {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

            } else {
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

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        } else if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            try {
                imageUri = data.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(imageBitmap);
                enabled = true;
                btnoku.setEnabled(enabled);
            } catch (IOException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Galeri", e.toString());
            }

        } else if (requestCode == 3 && resultCode == RESULT_OK) {

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(imageBitmap);
                enabled = true;
                btnoku.setEnabled(enabled);
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Kamera", e.toString());
            }

        }

    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
