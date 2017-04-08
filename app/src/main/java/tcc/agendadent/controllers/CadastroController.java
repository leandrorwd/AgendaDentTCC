package tcc.agendadent.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.DentistaBC;
import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.BuscaEnderecoCep;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.Utilitarios;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;
import static tcc.agendadent.servicos.DialogAux.loadingDialog;

/**
 * Created by natha on 02/02/2017.
 */

public class CadastroController {
    private static CadastroController INSTANCE;
    private Activity tela;
    private Bitmap fotoPerfilCadastro;
    private DentistaBC dentistaBC;
    private PacienteBC pacienteBC;

    private CadastroController(){
        dentistaBC = new DentistaBC();
        pacienteBC = new PacienteBC();
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
                            dialogOkSimples(tela,tela.getResources().getString(R.string.erro),tela.getResources().getString(R.string.cepInvalido));
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
            dialogOkSimples(activity,activity.getResources().getString(R.string.erro),activity.getResources().getString(R.string.CEPNaoencontrado));
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

    public void cadastraDentista(Activity activity, String email, String nome, String sobreNome, String senha1, String senha2,
                                 String inscricaoCro, String cep, String estado, String cidade,
                                 String bairro, String rua, String numero, String complemento,
                                 ImageView perfil, String celular) {
        String erro = activity.getResources().getString(R.string.erro);

        if(!ValidationTest.verificaInternet(activity)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.internetSemConexao));
            return;
        }
        if(!ValidationTest.validaString(email)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.emailVazio));
            return;
        }
        if(!ValidationTest.validaEmail(email)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.emailInvalido));
            return;
        }

        if(!ValidationTest.validaString(nome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.nomeVazio));
            return;
        }

        if(!ValidationTest.validaString(sobreNome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.sobreNomeVazio));
            return;
        }
        if(!ValidationTest.validaString(senha1)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senhaVazio));
            return;
        }
        if(!ValidationTest.validaString(senha2)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senhaVazio2));
            return;
        }
        if(!ValidationTest.validaSenhas(senha1,senha2)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senhaDiferen));
            return;
        }
        if(!ValidationTest.validaTamanhoSenha(senha1)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senha6Caracter));
            return;
        }


        if(!ValidationTest.validaString(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celularVazio));
            return;
        }
        if(!ValidationTest.validaTelefone(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celulaInvalido));
            return;
        }

        if(!ValidationTest.validaString(inscricaoCro)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.inscricaoCROVazio));
            return;
        }

        //TODO Validacao CORRETA do InscricaoCRO

        if(!ValidationTest.validaFotoTirada(perfil)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.fotoNaoRetirada));
            return;
        }

        if(!ValidationTest.validaString(cep)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.cepVazio));
            return;
        }

        if(!ValidationTest.validaCep(cep)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.cepInvalido));
            return;
        }

        if(!ValidationTest.validaString(estado)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.estadoVazio));
            return;
        }
        if(!ValidationTest.validaString(cidade)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.cidadeVazio));
            return;
        }
        if(!ValidationTest.validaString(bairro)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.bairroVazio));
            return;
        }
        if(!ValidationTest.validaString(rua)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.ruaVazio));
            return;
        }
        if(!ValidationTest.validaString(numero)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.numeroVazio));
            return;
        }
        if(!ValidationTest.validaNumeroPuro(numero)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.numeroInvalido));
            return;
        }
        String cepAux = cep.replace("-","");
        Endereco e1 = new Endereco("Brasil",estado,cidade,bairro,rua,complemento,
                Integer.parseInt(numero),Integer.parseInt(cepAux));
        setFotoPerfil(perfil);
        UsuarioDentista usuario = new UsuarioDentista(email,nome,sobreNome,inscricaoCro,"null",e1,celular);
        AutenticacaoController.getInstance().cadastraDentista(activity,usuario,senha1);
    }

    private void setFotoPerfil(ImageView perfil) {
        Bitmap bitmapPerfil = ((BitmapDrawable)perfil.getDrawable()).getBitmap();
        fotoPerfilCadastro = Utilitarios.getResizedBitmap(bitmapPerfil,500);
    }

    public void insertNewDentista(UsuarioDentista usuario,Activity tela) {
        dentistaBC.insertDentista(usuario,tela);
    }

    public Bitmap getFotoPerfilCadastro() {
        return fotoPerfilCadastro;
    }

    public void cadastraPaciente(Activity activity, String email, String nome, String sobreNome, String senha1, String senha2, String celular) {
        String erro = activity.getResources().getString(R.string.erro);

        if(!ValidationTest.verificaInternet(activity)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.internetSemConexao));
            return;
        }
        if(!ValidationTest.validaString(email)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.emailVazio));
            return;
        }
        if(!ValidationTest.validaEmail(email)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.emailInvalido));
            return;
        }

        if(!ValidationTest.validaString(nome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.nomeVazio));
            return;
        }

        if(!ValidationTest.validaString(sobreNome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.sobreNomeVazio));
            return;
        }
        if(!ValidationTest.validaString(senha1)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senhaVazio));
            return;
        }
        if(!ValidationTest.validaString(senha2)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senhaVazio2));
            return;
        }
        if(!ValidationTest.validaSenhas(senha1,senha2)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senhaDiferen));
            return;
        }
        if(!ValidationTest.validaTamanhoSenha(senha1)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.senha6Caracter));
            return;
        }

        if(!ValidationTest.validaString(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celularVazio));
            return;
        }
        if(!ValidationTest.validaTelefone(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celulaInvalido));
            return;
        }

        UsuarioPaciente usuario = new UsuarioPaciente(email,nome,sobreNome,celular);
        AutenticacaoController.getInstance().cadastraPaciente(activity,usuario,senha1);
    }

    public void insertNewPaciente(UsuarioPaciente usuario) {
        pacienteBC.insertPaciente(usuario);
    }


}
