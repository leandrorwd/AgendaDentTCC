package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteAgendarConsulta2 extends AppCompatActivity {
    private EditText nomeDentistaBusca;
    private Spinner tipoConsulta;
    private Spinner planosDeSaude;
    private Spinner especializacao;
    private Spinner spinnerEstado;
    private RadioGroup radioGroup;
    private LinearLayout proximoMim;
    private LinearLayout outroEndereco;
    private EditText cidadeDentista;
    private EditText bairroDentista;
    private EditText ruaDentista;
    private EditText numeroDentista;
    private EditText complementoDentista;
    private Button pesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_busca_dentista);
        setTitle("Agendar Consulta");
        instanciaArtefatos();
        setEventos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Your code here
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void instanciaArtefatos() {

        //back navigation
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarBuscaDentistas));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nomeDentistaBusca = (EditText) findViewById(R.id.nomeDentistaBusca);
        cidadeDentista = (EditText) findViewById(R.id.cidadeDentista);
        bairroDentista = (EditText) findViewById(R.id.bairroDentista);
        ruaDentista = (EditText) findViewById(R.id.ruaDentista);
        numeroDentista = (EditText) findViewById(R.id.numeroDentista);
        complementoDentista = (EditText) findViewById(R.id.complementoDentista);
        outroEndereco = (LinearLayout) findViewById(R.id.layoutOutroEndereco);
        proximoMim = (LinearLayout) findViewById(R.id.layoutProximoMim);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        tipoConsulta = (Spinner) findViewById(R.id.tipoConsulta);
        planosDeSaude = (Spinner) findViewById(R.id.idPlanosDeSaude);
        especializacao = (Spinner) findViewById(R.id.idSpinnerEspecialazacao);
        spinnerEstado = (Spinner) findViewById(R.id.idSpinnerEstado);
        pesquisar = (AppCompatButton) findViewById(R.id.botaoPesquisar);
        planosDeSaude.setVisibility(View.GONE);
        outroEndereco.setVisibility(View.GONE);
        proximoMim.setVisibility(View.GONE);
    }

    private void setEventos() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isChecked = ((RadioButton) group.findViewById(checkedId)).isChecked();
                if (isChecked && checkedId == R.id.idRadioProx) {
                    outroEndereco.setVisibility(View.GONE);
                    proximoMim.setVisibility(View.VISIBLE);
                }
                if (isChecked && checkedId == R.id.idRadioOutro) {
                    outroEndereco.setVisibility(View.VISIBLE);
                    proximoMim.setVisibility(View.GONE);
                }
            }
        });

        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planoSaude;
                if(!tipoConsulta.getSelectedItem().toString().equals("Convênio")){
                    planoSaude = "null";
                }
                else{
                    planoSaude =planosDeSaude.getSelectedItem().toString();
                }
                RadioButton r1 = (RadioButton) findViewById(R.id.idRadioProx);
                RadioButton r2 = (RadioButton) findViewById(R.id.idRadioOutro);

                int distancia = 0;
                Endereco e1 = null;
                if (r1.isChecked()){
                    //TODO DISTANCIA STUFF
                }
                else if(r2.isChecked()){
                    e1 = new Endereco("Brasil", spinnerEstado.getSelectedItem().toString(), cidadeDentista.getText().toString()
                            ,bairroDentista.getText().toString(), ruaDentista.getText().toString()
                            , complementoDentista.getText().toString(), 0,0 );
                }
                else{
                     distancia = 0;
                     e1 = null;
                }
                DialogAux.dialogCarregandoSimples(PacienteAgendarConsulta2.this);
                PacienteController.getInstance().getDentistasFiltro(PacienteAgendarConsulta2.this,nomeDentistaBusca.getText().toString(),
                        tipoConsulta.getSelectedItem().toString(),planoSaude,
                        especializacao.getSelectedItem().toString(),e1,distancia);
            }
        });
        tipoConsulta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (tipoConsulta.getSelectedItem().toString().equals("Convênio")) {
                    planosDeSaude.setVisibility(View.VISIBLE);
                } else
                    planosDeSaude.setVisibility(View.GONE);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}
