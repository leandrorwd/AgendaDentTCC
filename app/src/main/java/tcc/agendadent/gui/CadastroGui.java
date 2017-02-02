package tcc.agendadent.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.servicos.Foto;

public class CadastroGui extends AppCompatActivity {

    private EditText email;
    private EditText nome;
    private EditText sobreNome;
    private EditText senha;
    private EditText senha2;
    private EditText inscricaoCro;
    private EditText cep;
    private Spinner estado;
    private EditText cidade;
    private EditText bairro;
    private EditText rua;
    private EditText numero;
    private EditText complemento;
    private Foto foto;
    private ImageView perfil;
    private ImageView camera;
    private int contadorNumerosCep = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gui);
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        email = (EditText) findViewById(R.id.idCadastroEmail);
        nome = (EditText) findViewById(R.id.idCadastroNome);
        sobreNome = (EditText) findViewById(R.id.idCadastraSobreNome);
        senha = (EditText) findViewById(R.id.idCadastroSenha);
        senha2 = (EditText) findViewById(R.id.idCadastroSenha2);
        inscricaoCro = (EditText) findViewById(R.id.idCadastroCRO);
        cep = (EditText) findViewById(R.id.idCadastroCep);
        estado = (Spinner) findViewById(R.id.idCadastroSpinnerEstado);
        cidade = (EditText) findViewById(R.id.idCadastroCidade);
        bairro = (EditText) findViewById(R.id.idCadastroBairro);
        rua = (EditText) findViewById(R.id.idCadastroRua);
        numero = (EditText) findViewById(R.id.idCadastroNumero);
        complemento = (EditText) findViewById(R.id.idCadastroComplemento);
        perfil = (ImageView) findViewById(R.id.fotoTela);
        camera = (ImageView) findViewById(R.id.camera);


        foto = new Foto(CadastroGui.this);
    }
    private void setEventos() {
        CadastroController.getInstance().setActivity(CadastroGui.this);
        cep.addTextChangedListener(CepTextWatcher);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto.setFoto(CadastroGui.this);
            }
        });
    }

    private TextWatcher CepTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() == 6 && contadorNumerosCep == 5){
                cep.setText(cep.getText().toString().substring(0, s.length() -1) + "-" +
                        cep.getText().toString().substring(s.length() -1, s.length()));
                cep.setSelection(7);
            }
            else if(s.length() == 9){
                CadastroController.getInstance().buscaCep(CadastroGui.this,cep.getText().toString());
            }
            contadorNumerosCep = s.length();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        foto.selectImagem(requestCode, resultCode, data);
    }


}
