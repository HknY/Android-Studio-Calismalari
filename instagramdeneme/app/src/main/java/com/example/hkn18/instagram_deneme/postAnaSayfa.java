package com.example.hkn18.instagram_deneme;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class postAnaSayfa extends ArrayAdapter<String> {

    private final ArrayList<String> eposta;
    private final ArrayList<String> resim;
    private final ArrayList<String> not;
    private final Activity context;

    public postAnaSayfa(ArrayList<String> eposta, ArrayList<String> resim, ArrayList<String> not, Activity context) {
        super(context, R.layout.post, eposta);
        this.eposta = eposta;
        this.resim = resim;
        this.not = not;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View post_sayfa = layoutInflater.inflate(R.layout.post, null,true);

        EditText pposta = post_sayfa.findViewById(R.id.post_posta_txt);
        ImageView presim = post_sayfa.findViewById(R.id.post_resim_img);
        EditText pnot = post_sayfa.findViewById(R.id.post_aciklama_txt);

        pposta.setText(eposta.get(position));
        Picasso.get().load(resim.get(position)).into(presim);
        pnot.setText(not.get(position));


        return post_sayfa;
    }
}
