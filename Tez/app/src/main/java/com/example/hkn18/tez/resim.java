package com.example.hkn18.tez;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class resim extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    public static String deger = "Boş";

    Intent intent;
    Button btnoku, btnsec;
    ImageView imageView;
    Bitmap imageBitmap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = findViewById(R.id.imageView);
        btnoku = findViewById(R.id.btnoku);
        btnsec = findViewById(R.id.btnsec);
        btnsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimAl();
            }
        });
        btnoku.setEnabled(false);

        btnoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okuTxt();
                onBackPressed();
            }
        });

        if (deger.equals("Galeri")) btnsec.setText("Fotoğraf Seç");
        if (deger.equals("Kamera")) btnsec.setText("Fotoğraf Çek");
        if (deger.equals("Boş")) btnsec.setText("Hata!");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private static final int Resim_Galeri = 1;
    private static final int Resim_Kamera = 2;
    private Uri imageUri;
    private String imageurl;

    private void resimAl() {
        if (deger == "Galeri") {
            if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Resim_Galeri);
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Resim_Galeri);
            }
        }
        if (deger == "Kamera") {
            if (checkCallingOrSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Resim_Kamera);
            } else {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Resim_Kamera);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Resim_Galeri) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Bir Fotoğraf Seçin"), Resim_Galeri);
            }
        }

        if (requestCode == Resim_Kamera) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Resim_Kamera);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (deger == "Galeri") {
            try {
                if (requestCode == Resim_Galeri && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    imageBitmap = BitmapFactory.decodeFile(imgDecodableString);
                    String s = getRealPathFromURI(selectedImage);
                    File newFile = new File(s);
                    String path = newFile.getAbsolutePath();
                    btnoku.setEnabled(true);
                } else {
                    Toast.makeText(this, "Resim seçmediniz.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Resim seçilirken hata oluştu.", Toast.LENGTH_LONG).show();
            }
        }

        if (deger == "Kamera") {
            try {
                if (requestCode == Resim_Kamera && resultCode == Activity.RESULT_OK) {
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imageView.setImageBitmap(thumbnail);
                        imageurl = getRealPathFromURI(imageUri);
                        btnoku.setEnabled(true);
                    } catch (Exception e) {
                        Toast.makeText(this, "Resim seçmediniz.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Resim seçilirken hata oluştu.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void okuTxt() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result = detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                isleTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void isleTxt(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();

        if (blocks.size() == 0) {
            MainActivity.txtyazi.setText("");
            Toast.makeText(getApplicationContext(), "Okuyamadım!", Toast.LENGTH_LONG).show();
        }
        for (FirebaseVisionText.TextBlock blok : firebaseVisionText.getTextBlocks()) {
            String txt = blok.getText();
            MainActivity.txtyazi.setText(txt);
        }
    }
}
