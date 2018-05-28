package com.example.hkn18.listview;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list);

        ArrayList<String> arrayname = new ArrayList<>();
        ArrayList<Bitmap> arrayimage = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayname);
        listView.setAdapter(arrayAdapter);

        try {

            Main2Activity.database = this.openOrCreateDatabase("Resim", MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS resim (adi VARCHAR, image BLOB)");

            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM resim",null);

            int namex = cursor.getColumnIndex("adi");
            int imagex = cursor.getColumnIndex("image");

            cursor.moveToFirst();

            while (cursor != null){
                arrayname.add(cursor.getString(namex));

                //byte [] bytearray = cursor.getBlob(imagex);
               // Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
                //arrayimage.add(bitmap);
                cursor.moveToNext();
                arrayAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ekle, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.resim_ekle){
            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            intent.putExtra("info","yeni");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
