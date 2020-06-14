package com.ftn.mailClient.utill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class LoadImageAsyncTask extends CallbackAsyncTask<String, Void, Bitmap> {

    public LoadImageAsyncTask(OnPostExecuteFunctionFunctionalInterface<Bitmap> onPostExecuteFunctionFunctionalInterface) {
        super(onPostExecuteFunctionFunctionalInterface);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if(in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
