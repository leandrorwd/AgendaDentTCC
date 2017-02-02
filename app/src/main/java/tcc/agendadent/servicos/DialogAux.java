package tcc.agendadent.servicos;

/**
 * Created by natha on 02/02/2017.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import tcc.agendadent.R;


/**
 * Created by dalbem on 24/01/2017.
 */

public class DialogAux {
    public static ProgressDialog loadingDialog;

    public static void dialogOkSimples(Activity tela,String titulo, String mensagem){
        new AlertDialog.Builder(tela)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(tela.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
    }

    public static void dialogOkSimplesFinish(final Activity tela, String titulo, String mensagem){
        new AlertDialog.Builder(tela)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(tela.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        tela.finish();
                    }
                })
                .show();
    }

    public static void dialogBack(final Activity tela){
        new AlertDialog.Builder(tela)
                .setTitle(tela.getResources().getString(R.string.Aviso))
                .setMessage(tela.getResources().getString(R.string.Sair))
                .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        tela.finish();
                    }
                })
                .setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
    }

    public static void dialogCarregandoSimples(Activity tela) {
        loadingDialog = new ProgressDialog(tela);
        loadingDialog.setTitle(tela.getResources().getString(R.string.Aguarde));
        loadingDialog.setMessage(tela.getResources().getString(R.string.Carregando));
        loadingDialog.setCancelable(true);
        loadingDialog.show();
    }
    public static void dialogCarregandoSimplesDismiss() {
        loadingDialog.dismiss();
    }




}

