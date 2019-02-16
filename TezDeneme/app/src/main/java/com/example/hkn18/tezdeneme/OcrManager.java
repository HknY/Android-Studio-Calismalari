package com.example.hkn18.tezdeneme;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrManager {

    String datapath;
    private TessBaseAPI mTess;
    public static MainActivity instance = null;

    public OcrManager(Context context) {
        // TODO Auto-generated constructor stub
        mTess = new TessBaseAPI();
        datapath = Environment.getExternalStorageDirectory() + "/tessdata/";
        //datapath = context.getFilesDir() + "/tesseract/";
        String language = "tur";
        //mTess.setDebug(true);
        /*File dir = new File(datapath + "tur.traineddata");
        if (!dir.exists())
            dir.mkdirs();*/
        mTess.init(datapath, language);
    }
    public String getOCRResult(Bitmap bitmap) {

        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();

        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }





/*
    TessBaseAPI baseAPI = null;

    public void initApi(String path) {
        String datapath = Environment.getExternalStorageDirectory() + "/com.example.hkn18.tezdeneme/";
        baseAPI = new TessBaseAPI();
        baseAPI.init(datapath,"tur");
    }

    public String startRecognize(Bitmap bitmap, String path){
        if(baseAPI == null)
            initApi(path);
        baseAPI.setImage(bitmap);
        return baseAPI.getUTF8Text();
    }
    */
}
