package tcc.agendadent.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.Foto;

public class CadastroGui extends AppCompatActivity {

    private Spinner tipoUsuario;
    private EditText email;
    private EditText nome;
    private EditText sobreNome;
    private EditText senha;
    private EditText senha2;
    private EditText inscricaoCro;
    private EditText celular;
    private EditText cep;
    private Spinner estado;
    private Spinner sexo;

    private EditText cidade;
    private EditText bairro;
    private EditText rua;
    private EditText numero;
    private EditText complemento;
    private Foto foto;
    private ImageView perfil;
    private ImageView camera;
    private Button botaoCadastro;
    private LinearLayout layoutDentista;
    private int contadorNumerosCep = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gui);
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        tipoUsuario = (Spinner) findViewById(R.id.idCadastroTipoUser);
        email = (EditText) findViewById(R.id.idCadastroEmail);
        nome = (EditText) findViewById(R.id.idCadastroNome);
        sobreNome = (EditText) findViewById(R.id.idCadastraSobreNome);
        senha = (EditText) findViewById(R.id.idCadastroSenha);
        senha2 = (EditText) findViewById(R.id.idCadastroSenha2);
        inscricaoCro = (EditText) findViewById(R.id.idCadastroCRO);
        celular = (EditText) findViewById(R.id.idCelular);
        cep = (EditText) findViewById(R.id.idCadastroCep);
        estado = (Spinner) findViewById(R.id.idCadastroSpinnerEstado);
        sexo = (Spinner) findViewById(R.id.idCadastroSexo);
        cidade = (EditText) findViewById(R.id.idCadastroCidade);
        bairro = (EditText) findViewById(R.id.idCadastroBairro);
        rua = (EditText) findViewById(R.id.idCadastroRua);
        numero = (EditText) findViewById(R.id.idCadastroNumero);
        complemento = (EditText) findViewById(R.id.idCadastroComplemento);
        perfil = (ImageView) findViewById(R.id.fotoTela);
        camera = (ImageView) findViewById(R.id.camera);
        botaoCadastro = (Button) findViewById(R.id.botaoCadastro) ;
        layoutDentista = (LinearLayout) findViewById(R.id.exclusivoDentista);

        perfil.setTag("default");
        foto = new Foto(CadastroGui.this);
        layoutDentista.setVisibility(View.GONE);

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

        celular.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if(tipoUsuario.getSelectedItem().toString().equals("Dentista")){
                     layoutDentista.setVisibility(View.VISIBLE);
                 }
                else{
                     layoutDentista.setVisibility(View.GONE);
                 }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAux.dialogCarregandoSimples(CadastroGui.this);
                boolean masculino=false;
                if(sexo.getSelectedItem().toString().equals("Masculino"))
                    masculino = true;
                if(tipoUsuario.getSelectedItem().toString().equals("Dentista")){
                    CadastroController.getInstance().cadastraDentista(CadastroGui.this,email.getText().toString(),
                            nome.getText().toString(),sobreNome.getText().toString(),senha.getText().toString(),
                            senha2.getText().toString(),inscricaoCro.getText().toString(),cep.getText().toString(),
                            (String) estado.getItemAtPosition(estado.getSelectedItemPosition()),cidade.getText().toString(),
                            bairro.getText().toString(),rua.getText().toString(),numero.getText().toString(),
                            complemento.getText().toString(),perfil,celular.getText().toString(),masculino);
                            }
                else{
                        CadastroController.getInstance().cadastraPaciente(CadastroGui.this,email.getText().toString(),
                                nome.getText().toString(),sobreNome.getText().toString(),senha.getText().toString(),
                                senha2.getText().toString(),celular.getText().toString(),masculino);
                }
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
