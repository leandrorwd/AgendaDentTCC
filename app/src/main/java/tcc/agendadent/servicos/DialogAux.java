package tcc.agendadent.servicos;

/**
 * Created by natha on 02/02/2017.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import tcc.agendadent.R;
import tcc.agendadent.gui.dentista.Main_Dentista;
import tcc.agendadent.gui.paciente.Main_Paciente;


public class DialogAux {
    public static ProgressDialog loadingDialog;
    private static boolean aberto;
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

    public static void dialogOkSimplesInnerClassDentistaFinish(final Activity tela, String titulo, String mensagem){
        new AlertDialog.Builder(tela)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(tela.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Main_Dentista.animacaoTrocaJanelaVoltaExterno();
                    }
                })
                .show();
    }

    public static void dialogOkSimplesInnerClassPacienteFinish(final Activity tela, String titulo, String mensagem){
        new AlertDialog.Builder(tela)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(tela.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Main_Paciente.animacaoTrocaJanelaVoltaExterno();
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
        try{
            if(aberto) return;
            aberto = true;
            loadingDialog = new ProgressDialog(tela);
            loadingDialog.setTitle(tela.getResources().getString(R.string.Aguarde));
            loadingDialog.setMessage(tela.getResources().getString(R.string.Carregando));
            loadingDialog.setCancelable(true);
            loadingDialog.show();
        }
        catch (Exception e){}
    }
    public static void dialogCarregandoSimplesDismiss() {
        aberto = false;
        try{
            loadingDialog.dismiss();
         }catch (Exception e){}
    }


    public static void dialogExcecoesFirebase(Activity tela,Exception excep,String email){
        String erro = tela.getResources().getString(R.string.erro);
        try {
            throw excep;
        } catch(FirebaseAuthWeakPasswordException e) {
            dialogOkSimples(tela,erro,tela.getResources().getString(R.string.senhaFracaFireBase));
            dialogCarregandoSimplesDismiss();
        } catch(FirebaseAuthInvalidCredentialsException e) {
            dialogOkSimples(tela,erro,tela.getResources().getString(R.string.emailInvalido));
            dialogCarregandoSimplesDismiss();
        } catch(FirebaseAuthUserCollisionException e) {
            dialogOkSimples(tela,erro,tela.getResources().getString(R.string.emailCadastrado,email));
            dialogCarregandoSimplesDismiss();
        } catch(Exception e) {
            dialogOkSimples(tela,erro,(e.getMessage()));
            dialogCarregandoSimplesDismiss();
        }
    }

}

