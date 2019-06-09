package com.example.deneme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int ortalama = 0;
    Intent git;
    public static TextView textView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("gida");
    public static ArrayMap<String, String> veritabani = new ArrayMap<>();
    public static ArrayList<Egida> E_Gida = new ArrayList<Egida>();

    ListView list;
    ListviewAdapter adapter;
    SearchView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        final TextView txt_sonuc = findViewById(R.id.txt_sonuc);

        ImageButton imgbtn_galeri = findViewById(R.id.imgbtn_galeri);
        ImageButton imgbtn_video = findViewById(R.id.imgbtn_video);
        ImageButton imgbtn_kamera = findViewById(R.id.imgbtn_kamera);

        Button btnDeger = findViewById(R.id.btnDeger);

        final ProgressBar bar = findViewById(R.id.progressBar);

        list = findViewById(R.id.list_view);
        editsearch = findViewById(R.id.search_view);

        editsearch.setIconifiedByDefault(false);
        editsearch.setFocusable(false);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    veritabani.put(ds.getKey(), ds.getValue().toString());
                    Egida gida = new Egida(ds.getKey(), ds.getValue().toString());
                    E_Gida.add(gida);
                }

                adapter = new ListviewAdapter(getApplicationContext(), E_Gida);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String ici = textView.getText().toString();
                        String[] icinde = ici.split(", ");
                        boolean deger = true;

                        for (int i = 0; i < icinde.length; i++) {

                            String yazi = icinde[i];

                            if (i == 0) {
                                if (icinde[i].split(" ").length > 1)
                                    yazi = icinde[i].split(" ")[1];
                            }

                            if (yazi.equals(E_Gida.get(position).getGida())) {
                                deger = false;
                            }

                        }

                        if (deger)
                            textView.setText(textView.getText() + E_Gida.get(position).getGida() + ", ");

                        editsearch.clearFocus();
                        editsearch.setQuery("", false);
                    }
                });


                editsearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus)
                            list.setVisibility(View.VISIBLE);
                        else
                            list.setVisibility(View.INVISIBLE);
                    }
                });

                editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.filter(newText);
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imgbtn_galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("İçindekiler: ");
                Fotograf.btnYazi = "Galeri";
                git = new Intent(getApplicationContext(), Fotograf.class);
                startActivity(git);
                Fotograf.enabled = false;
            }
        });

        imgbtn_kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("İçindekiler: ");
                Fotograf.btnYazi = "Kamera";
                git = new Intent(getApplicationContext(), Fotograf.class);
                startActivity(git);
                Fotograf.enabled = false;
            }
        });
        textView.setText(textView.getText() + Fotograf.yazi);

        imgbtn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("İçindekiler: ");
                git = new Intent(getApplicationContext(), Kamera.class);
                startActivity(git);
            }
        });


        btnDeger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_sonuc.setText("Sonuç: ");
                ortalama = 0;

                String[] icindekiler = textView.getText().toString().split(", ");
                String sonuc = " ";

                for (int j = 0; j < icindekiler.length; j++) {
                    boolean deger = true;

                    for (int i = 0; i < E_Gida.size(); i++) {
                        String yazi = icindekiler[j];

                        if (j == 0) {
                            if (yazi.split(": ").length > 1) {
                                yazi = icindekiler[j].split(": ")[1];
                            }
                        }

                        if (yazi.equals(E_Gida.get(i).getGida()) && deger) {
                            sonuc += E_Gida.get(i).getDeger() + " ";
                            ortalama += Integer.parseInt(E_Gida.get(i).getDeger());
                            deger = false;
                        }

                    }

                }

                bar.setProgress(Math.round((float) ortalama / icindekiler.length));

                txt_sonuc.setText(txt_sonuc.getText() + sonuc);
            }
        });

    }

}
