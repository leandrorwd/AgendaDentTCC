package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.servicos.DialogAux;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

/**
 * Created by natha on 04/05/2017.
 */

public class PacienteEditarEndereco extends LinearLayout implements ClassesPaciente {
    private int id_janela;
    private EditText cep;
    private static Spinner spinnerEstado;
    private static EditText cidade;
    private static EditText bairro;
    private static EditText rua;
    private static EditText numero;
    private static EditText complemento;
    private AppCompatButton botaoSalvar;
    private Activity activity;


    public PacienteEditarEndereco(Activity activity, int id_janela) {
        super(activity);
        this.id_janela = id_janela;
        DialogAux.dialogCarregandoSimples(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_endereco, this);
        instanciaArtefatos();
        setEventos();
        carregaPaciente();
    }

    private void carregaPaciente() {
        if(PacienteController.getInstance().getPacienteLogado().getEndereco()!=null){
            String cepAux = PacienteController.getInstance().getPacienteLogado().getEndereco().getCep()+"";
            cep.setText(cepAux);
            spinnerEstado.setSelection(((ArrayAdapter<String>)spinnerEstado.getAdapter()).getPosition(PacienteController.getInstance().getPacienteLogado().getEndereco().getEstado()));
            cidade.setText(PacienteController.getInstance().getPacienteLogado().getEndereco().getCidade());
            bairro.setText(PacienteController.getInstance().getPacienteLogado().getEndereco().getBairro());
            rua.setText(PacienteController.getInstance().getPacienteLogado().getEndereco().getRua());
            numero.setText(PacienteController.getInstance().getPacienteLogado().getEndereco().getNumero()+"");
            complemento.setText(PacienteController.getInstance().getPacienteLogado().getEndereco().getComplemento());
        }
        DialogAux.dialogCarregandoSimplesDismiss();

    }

    private void setEventos() {
        botaoSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAux.dialogCarregandoSimples(activity);
                PacienteController.getInstance().salvaEndereco(activity,
                         cep.getText().toString(),
                        (String) spinnerEstado.getItemAtPosition(spinnerEstado.getSelectedItemPosition()),cidade.getText().toString(),
                        bairro.getText().toString(),rua.getText().toString(),numero.getText().toString(),
                        complemento.getText().toString());
            }
        });
        cep.addTextChangedListener(CepTextWatcher);

    }

    private void instanciaArtefatos() {

        cep = (EditText) findViewById(R.id.cep);
        cidade = (EditText) findViewById(R.id.cidadeDentista);
        spinnerEstado = (Spinner) findViewById(R.id.idSpinnerEstado);
        bairro = (EditText) findViewById(R.id.bairroDentista);
        rua = (EditText) findViewById(R.id.ruaDentista);
        numero = (EditText) findViewById(R.id.numeroDentista);
        complemento = (EditText) findViewById(R.id.complementoDentista);
        botaoSalvar = (AppCompatButton) findViewById(R.id.botaoSalvar);
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

    private int contadorNumerosCep = 0;

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
                CadastroController.getInstance().buscaCep(activity,cep.getText().toString());
            }
            contadorNumerosCep = s.length();
        }
    };

    public static void setCep(Activity activity, Endereco endereco) {
        if(endereco == null){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,activity.getResources().getString(R.string.erro),activity.getResources().getString(R.string.CEPNaoencontrado));
        }
        else{
            spinnerEstado.setSelection(((ArrayAdapter<String>)spinnerEstado.getAdapter()).getPosition(endereco.getEstado()));
            cidade.setText(endereco.getCidade());
            bairro.setText(endereco.getBairro());
            rua.setText(endereco.getRua());
            numero.setText(endereco.getNumero()+"");
            complemento.setText(endereco.getComplemento());
            DialogAux.dialogCarregandoSimplesDismiss();

        }
    }
}
