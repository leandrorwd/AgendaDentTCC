package tcc.agendadent.servicos;

/**
 * Created by Work on 08/04/2017.
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;



/**
 * Created by dalbem on 18/11/2016.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String nomeTela;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        nomeTela="";
    }
    public DownloadImageTask(ImageView bmImage, Activity tela) {
        this.bmImage = bmImage;
        nomeTela = tela.getLocalClassName();
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
