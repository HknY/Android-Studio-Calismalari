package com.example.facedeneme;

public class Kamera {

    private int cozunurlük = 0;

    public Ekran mEkran() {
        return mekran;
    }

    private final Ekran mekran = new Ekran() {

        @Override
        public void mCamera(Camera camera) {

        }

    };

    public void setCozunurluk(int sayi){
        cozunurlük=sayi;
    }

    public int getCozunurluk(){
        return cozunurlük;
    }

    public static class Ozellik extends Kamera {

        public Kamera.Ozellik Cozunurluk(int sayi) {
            setCozunurluk(sayi);
            return this;
        }

        public Kamera.Ozellik Ayar() {
            return this;
        }

    }

}


