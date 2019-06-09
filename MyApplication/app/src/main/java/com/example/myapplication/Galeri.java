package com.example.myapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class Galeri extends Fragment {


    public Galeri() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_galeri, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button2).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_galeri_to_anaSayfa));
        view.findViewById(R.id.button3).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_galeri_to_kamera));
        view.findViewById(R.id.button4).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_galeri_to_asd));
    }
}
