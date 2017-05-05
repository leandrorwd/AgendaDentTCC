package tcc.agendadent.servicos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import tcc.agendadent.objetos.Endereco;

/**
 * Created by natha on 07/02/2017.
 */

public class Utilitarios {
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static double[] getLatitudeLongitude(Activity activity,Endereco e1){
        Geocoder coder = new Geocoder(activity, Locale.getDefault());
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName("Brasil,"+e1.toString(), 1);
            for(Address add : adresses){
                double[] latLong = new double[2];
                latLong[1] = add.getLongitude();
                latLong[0] = add.getLatitude();
                return latLong;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
