package tcc.agendadent.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.gui.CadastroGui;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.servicos.BuscaEnderecoCep;
import tcc.agendadent.servicos.DialogAux;

import static tcc.agendadent.servicos.DialogAux.loadingDialog;

/**
 * Created by natha on 02/02/2017.
 */

public class CadastroController {
    private static CadastroController INSTANCE;
    private Activity tela;

    private CadastroController(){

    }
    public static CadastroController getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CadastroController();
        }
        return INSTANCE;
    }

    public void buscaCep(final Activity tela, final String cep) {
        new AlertDialog.Builder(tela)
                .setTitle(tela.getResources().getString(R.string.BuscarCEP))
                .setMessage(tela.getResources().getString(R.string.PerguntaBuscarCEP))
                .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DialogAux.dialogCarregandoSimples(tela);
                        if(cep.indexOf("-") == -1){
                            loadingDialog.dismiss();
                            DialogAux.dialogOkSimples(tela,tela.getResources().getString(R.string.erro),tela.getResources().getString(R.string.cepInvalido));
                        }
                        else{
                            BuscaEnderecoCep.getInstance().BuscaEndereco(cep,tela);
                        }
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

    public void setCep(Activity activity, Endereco endereco) {
        if(endereco == null){
            DialogAux.dialogCarregandoSimplesDismiss();
            DialogAux.dialogOkSimples(activity,activity.getResources().getString(R.string.erro),activity.getResources().getString(R.string.CEPNaoencontrado));
        }
        else{
            if(activity.getLocalClassName().equals("gui.CadastroGui")){
                EditText editText = (EditText) activity.findViewById(R.id.idCadastroRua);
                editText.setText(endereco.getRua().toString());
                editText = (EditText) activity.findViewById(R.id.idCadastroBairro);
                editText.setText(endereco.getBairro().toString());
                editText = (EditText) activity.findViewById(R.id.idCadastroCidade);
                editText.setText(endereco.getCidade().toString());
                Spinner estado = (Spinner) activity.findViewById(R.id.idCadastroSpinnerEstado);
                ArrayAdapter<String> array_spinner = (ArrayAdapter<String>)estado.getAdapter();
                estado.setSelection(array_spinner.getPosition(endereco.getEstado()));
                editText = (EditText) activity.findViewById(R.id.idCadastroComplemento);
                editText.setText(endereco.getComplemento().toString());
                loadingDialog.dismiss();
            }
        }
    }

    public void setImagemPerfilDentista(Bitmap foto) {
        ImageView perfil = (ImageView) tela.findViewById(R.id.fotoTela);
        perfil.setImageBitmap(foto);
    }

    public void setActivity(Activity tela) {
        this.tela=tela;
    }
}
