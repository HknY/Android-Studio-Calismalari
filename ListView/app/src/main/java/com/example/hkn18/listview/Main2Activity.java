package com.example.hkn18.listview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    Button button;

    Bitmap secilen;
    static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = findViewById(R.id.resim);
        editText = findViewById(R.id.editText2);
        button = findViewById(R.id.button);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if(info.equalsIgnoreCase("yeni")){
            button.setVisibility(View.VISIBLE);
            editText.setText("");
        } else{
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void resim_sec(View view){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 2){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri resim = data.getData();
            try {
                secilen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resim);
                imageView.setImageBitmap(secilen);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void kaydet(View view){
        String isim = editText.getText().toString();

        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        secilen.compress(Bitmap.CompressFormat.PNG, 50,byteArrayInputStream );
        byte [] btyeArray = byteArrayInputStream.toByteArray();

        try {

            database = this.openOrCreateDatabase("Resim", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS resim (adi VARCHAR, image BLOB)");

            String sql = "INSERT INTO resim (adi, image) VALUES (?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sql);
            sqLiteStatement.bindString(1, isim);
            sqLiteStatement.bindBlob(2, btyeArray);
            sqLiteStatement.execute();

        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
