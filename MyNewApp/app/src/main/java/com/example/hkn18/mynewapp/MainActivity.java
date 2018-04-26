package com.example.hkn18.mynewapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText ad;
    EditText soyad;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.hkn18.mynewapp", Context.MODE_PRIVATE);

        ad = (EditText) findViewById(R.id.editText2);
        soyad = (EditText) findViewById(R.id.editText3);
    }


    public void degis(View view){

        if(ad.getText().toString().trim().equals("") || soyad.getText().toString().trim().equals("")){

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hata");
            alert.setMessage("Alanlar Boş");
            alert.show();
        }
        else{
            sharedPreferences.edit().putString("ad",ad.getText().toString()).apply();
            sharedPreferences.edit().putString("soyad",soyad.getText().toString()).apply();

            Intent intent = new Intent(getApplicationContext(), giris.class);
            intent.putExtra("ad",sharedPreferences.getString("ad","Hatalı Ad"));
            intent.putExtra("soyad", sharedPreferences.getString("soyad", "Hatalı Soyad"));
            startActivity(intent);
        }
    }

}
