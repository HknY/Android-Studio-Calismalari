package com.example.deneme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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
    static TextView txt_bilgi;
    static TextView txt_sonuc;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("gida");
    public static ArrayMap<String, String> veritabani = new ArrayMap<>();
    public static ArrayList<Egida> E_Gida = new ArrayList<Egida>();
    static ProgressBar bar;
    ListView list;
    ListviewAdapter adapter;
    SearchView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConstraintLayout layout = findViewById(R.id.layout);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        Log.d("asdasd", String.valueOf(layout.getHeight()));

        bar = findViewById(R.id.progressBar);

        textView = findViewById(R.id.text_view);
        txt_bilgi = findViewById(R.id.txt_bilgi);
        txt_sonuc = findViewById(R.id.txt_sonuc);

        bilgi();

        ImageButton imgbtn_galeri = findViewById(R.id.imgbtn_galeri);
        ImageButton imgbtn_video = findViewById(R.id.imgbtn_video);
        ImageButton imgbtn_kamera = findViewById(R.id.imgbtn_kamera);

        Button btnDeger = findViewById(R.id.btnDeger);


        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] yazi = textView.getText().toString().split(", ");

                if (yazi[0].split(": ").length > 1) {

                    final String[] liste = new String[yazi.length];
                    liste[0] = yazi[0].split(": ")[1];

                    for (int i = 1; i < yazi.length; i++) {
                        liste[i] = yazi[i];
                    }

                    final ArrayList itemsSelected = new ArrayList();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Silmek İstediğinizi Seçiniz");

                    builder.setMultiChoiceItems(liste, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                itemsSelected.add(which);
                            } else if (itemsSelected.contains(which)) {
                                itemsSelected.remove(Integer.valueOf(which));
                            }
                        }
                    }).setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textView.setText("İçindekiler: ");

                            for (int i = 0; i < liste.length; i++) {
                                boolean deger = true;

                                for (int j = 0; j < itemsSelected.size(); j++) {
                                    if (String.valueOf(i).equals(itemsSelected.get(j).toString()))
                                        deger = false;

                                }

                                if (deger)
                                    textView.setText(textView.getText() + liste[i] + ", ");

                            }
                            bilgi();
                        }
                    }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                }

                return false;
            }
        });

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

                        bilgi();
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
                boolean sifir = false;

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

                            if(E_Gida.get(i).getDeger().equals("0") || sifir){
                                sonuc = "0";
                                ortalama = 0;
                                sifir = true;
                            }else{
                                sonuc += E_Gida.get(i).getDeger() + " ";
                                ortalama += Integer.parseInt(E_Gida.get(i).getDeger());
                                deger = false;
                            }

                        }

                    }

                }

                int deger = Math.round((float) ortalama / icindekiler.length);
                bar.setProgress(deger);

                switch (deger) {
                    case 0:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için KESİNLİKLE önermiyoruz");
                        break;
                    case 1:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için KESİNLİKLE önermiyoruz");
                        break;
                    case 2:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için önermiyoruz");
                        break;
                    case 3:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için önermiyoruz");
                        break;
                    case 4:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için muadil ürünlere bakmanızı öneriyoruz");
                        break;
                    case 5:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için muadil ürünlere bakmanızı öneriyoruz");
                        break;
                    case 6:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için öneriyoruz");
                        break;
                    case 7:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için öneriyoruz");
                        break;
                    case 8:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için rahatlıkla öneriyoruz");
                        break;
                    case 9:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için rahatlıkla öneriyoruz");
                        break;
                    case 10:
                        txt_sonuc.setText(txt_sonuc.getText() + "Sağlığınız için MUTLAKA öneriliyor");
                        break;
                }

                //txt_sonuc.setText(txt_sonuc.getText() + sonuc);
            }
        });

    }

    public void bilgi() {

        String[] yazi = textView.getText().toString().split(", ");

        if (yazi[0].split(": ").length < 2) {
            txt_bilgi.setVisibility(View.INVISIBLE);
            bar.setProgress(0);
            txt_sonuc.setText("Sonuç: ");
        } else
            txt_bilgi.setVisibility(View.VISIBLE);

    }

}
