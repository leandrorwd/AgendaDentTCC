package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.DownloadImageTask;
import tcc.agendadent.servicos.Foto;

public class PerfilDentista extends LinearLayout implements ClassesDentista   {
    private int id_janela;
    private ImageView camera;
    private ImageView fotoTela;
    private EditText nomeDentista;
    private EditText sobrenomeDentista;
    private EditText telefoneDentista;
    private EditText inscricaoCRO;
    private EditText cep;
    private Spinner spinnerEstado;
    private EditText cidadeDentista;
    private EditText bairroDentista;
    private EditText ruaDentista;
    private EditText numeroDentista;
    private EditText complementoDentista;
    private AppCompatButton botaoSalvar;
    private Activity activity;
    private Foto foto;

    public PerfilDentista(Activity activity, int id_janela) {
        super(activity);
        this.id_janela = id_janela;
        DialogAux.dialogCarregandoSimples(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_perfil_dentista, this);
        instanciaArtefatos();
        setEventos();
        carregaDentista();
    }

    private void carregaDentista() {
        nomeDentista.setText(DentistaController.getInstance().getDentistaLogado().getNome());
        sobrenomeDentista.setText(DentistaController.getInstance().getDentistaLogado().getSobreNome());
        telefoneDentista.setText(DentistaController.getInstance().getDentistaLogado().getTelefone());
        inscricaoCRO.setText(DentistaController.getInstance().getDentistaLogado().getInscricaoCRO());
        String cepAux = DentistaController.getInstance().getDentistaLogado().getEndereco().getCep()+"";
        cep.setText(cepAux);
        spinnerEstado.setSelection(((ArrayAdapter<String>)spinnerEstado.getAdapter()).getPosition(DentistaController.getInstance().getDentistaLogado().getEndereco().getEstado()));
        DentistaController.getInstance().setImagemPerfilLoading(activity,DentistaController.getInstance().getDentistaLogado().getUrlFotoPerfil()
        ,fotoTela);
        cidadeDentista.setText(DentistaController.getInstance().getDentistaLogado().getEndereco().getCidade());
        bairroDentista.setText(DentistaController.getInstance().getDentistaLogado().getEndereco().getBairro());
        ruaDentista.setText(DentistaController.getInstance().getDentistaLogado().getEndereco().getRua());
        numeroDentista.setText(DentistaController.getInstance().getDentistaLogado().getEndereco().getNumero()+"");
        complementoDentista.setText(DentistaController.getInstance().getDentistaLogado().getEndereco().getComplemento());
        DialogAux.dialogCarregandoSimplesDismiss();
    }



    private void instanciaArtefatos() {
        camera = (ImageView) findViewById(R.id.camera);
        fotoTela = (ImageView) findViewById(R.id.fotoTela);
        nomeDentista = (EditText) findViewById(R.id.nomeDentista);
        sobrenomeDentista = (EditText) findViewById(R.id.sobrenomeDentista);
        telefoneDentista = (EditText) findViewById(R.id.telefoneDentista);
        inscricaoCRO = (EditText) findViewById(R.id.inscricaoCRO);
        cep = (EditText) findViewById(R.id.cep);
        cidadeDentista = (EditText) findViewById(R.id.cidadeDentista);
        spinnerEstado = (Spinner) findViewById(R.id.idSpinnerEstado);
        bairroDentista = (EditText) findViewById(R.id.bairroDentista);
        ruaDentista = (EditText) findViewById(R.id.ruaDentista);
        numeroDentista = (EditText) findViewById(R.id.numeroDentista);
        complementoDentista = (EditText) findViewById(R.id.complementoDentista);
        botaoSalvar = (AppCompatButton) findViewById(R.id.botaoSalvar);
        foto = new Foto(activity);

    }
    private void setEventos() {
        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                foto.setFoto(activity);
            }
        });

        botaoSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAux.dialogCarregandoSimples(activity);
                DentistaController.getInstance().salvaPerfilDentista(activity,
                        nomeDentista.getText().toString(),sobrenomeDentista.getText().toString(),inscricaoCRO.getText().toString(),
                        cep.getText().toString(),
                        (String) spinnerEstado.getItemAtPosition(spinnerEstado.getSelectedItemPosition()),cidadeDentista.getText().toString(),
                        bairroDentista.getText().toString(),ruaDentista.getText().toString(),numeroDentista.getText().toString(),
                        complementoDentista.getText().toString(),fotoTela,telefoneDentista.getText().toString());
            }
        });

    }

    @Override
    public void onResume() {

    }

    @Override
    public boolean needResume() {
        return false;
    }

    @Override
    public int getIdMenu() {
        return id_janela;
    }

    @Override
    public void flipper(boolean next) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        foto.selectImagem(requestCode, resultCode, data);
    }
}