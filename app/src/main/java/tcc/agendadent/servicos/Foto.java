package tcc.agendadent.servicos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tcc.agendadent.R;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.gui.CadastroGui;

/**
 * Created by natha on 02/02/2017.
 */

public class Foto {
    public static int REQUEST_CAMERA = 1;
    public static int SELECT_FILE = 1;
    protected Context context;
    private Activity activity;
    String userChoosenTask;


    public Foto(Activity tela) {
        context = tela;
        activity = tela;
    }

    public void selectImagem(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
            {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA)
            {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        Activity activity = (Activity)context;
        try
        {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (activity.getLocalClassName().equals("gui.CadastroGui")) {
            CadastroController.getInstance().setImagemPerfilDentista(thumbnail);
            ImageView aux = (ImageView) activity.findViewById(R.id.fotoTela);
            aux.setTag("concluiu");
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        Activity activity = (Activity)context;
        if (data != null)
        {
            try
            {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (activity.getLocalClassName().equals("gui.CadastroGui")) {
            CadastroController.getInstance().setImagemPerfilDentista(bm);
            ImageView aux = (ImageView) activity.findViewById(R.id.fotoTela);
            aux.setTag("concluiu");
        }
    }

    public void setFoto(Activity tela)
    {
        activity = tela;
        final CharSequence[] items = {context.getResources().getString(R.string.headerDialogFoto), context.getResources().getString(R.string.subTitleDialogFoto), context.getResources().getString(R.string.cancelDialogFoto)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.titleDialogFoto));
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                boolean result = Permissoes.checkPermission(context);
                if (items[item].equals(context.getResources().getString(R.string.headerDialogFoto)))
                {
                    userChoosenTask=context.getResources().getString(R.string.headerDialogFoto);
                    if(result)
                        cameraIntent();
                }
                else if (items[item].equals(context.getResources().getString(R.string.subTitleDialogFoto)))
                {
                    userChoosenTask=context.getResources().getString(R.string.subTitleDialogFoto);
                    if(result)
                        galleryIntent();
                }
                else if (items[item].equals(context.getResources().getString(R.string.cancelDialogFoto)))
                    dialog.dismiss();
            }
        });
        builder.show();
    }

    public void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Activity activity = (Activity) context;
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Activity activity = (Activity) context;
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
}
