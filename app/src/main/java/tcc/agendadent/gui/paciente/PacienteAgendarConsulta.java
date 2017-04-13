package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import tcc.agendadent.R;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteAgendarConsulta extends LinearLayout implements ClassesPaciente{
    private Activity activity;
    private Button botaopesquisar;
    private int id;
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



    public PacienteAgendarConsulta(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_busca_dentista, this);
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        nomeDentistaBusca=  (EditText) findViewById(R.id.nomeDentistaBusca);
        cidadeDentista=  (EditText) findViewById(R.id.cidadeDentista);
        bairroDentista=  (EditText) findViewById(R.id.bairroDentista);
        ruaDentista=  (EditText) findViewById(R.id.ruaDentista);
        numeroDentista=  (EditText) findViewById(R.id.numeroDentista);
        complementoDentista=  (EditText) findViewById(R.id.complementoDentista);
        outroEndereco=  (LinearLayout) findViewById(R.id.layoutOutroEndereco);
        proximoMim=  (LinearLayout) findViewById(R.id.layoutProximoMim);
        radioGroup=  (RadioGroup) findViewById(R.id.radioGroup);
        tipoConsulta=  (Spinner) findViewById(R.id.tipoConsulta);
        planosDeSaude=  (Spinner) findViewById(R.id.idPlanosDeSaude);
        especializacao=  (Spinner) findViewById(R.id.idSpinnerEspecialazacao);
        spinnerEstado=  (Spinner) findViewById(R.id.idSpinnerEstado);
        pesquisar=  (AppCompatButton) findViewById(R.id.botaoPesquisar);
        planosDeSaude.setVisibility(View.GONE);
        outroEndereco.setVisibility(View.GONE);
        proximoMim.setVisibility(View.GONE);

    }

    private void setEventos() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                boolean isChecked = ((RadioButton)group.findViewById(checkedId)).isChecked();
                if (isChecked && checkedId == R.id.idRadioProx)
                {
                    outroEndereco.setVisibility(View.GONE);
                    proximoMim.setVisibility(View.VISIBLE);
                }
                if (isChecked && checkedId == R.id.idRadioOutro)
                {
                    outroEndereco.setVisibility(View.VISIBLE);
                    proximoMim.setVisibility(View.GONE);
                }
            }
        });
        pesquisar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        tipoConsulta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(tipoConsulta.getSelectedItem().toString().equals("Convênio")){
                    planosDeSaude.setVisibility(View.VISIBLE);
                }
                else
                    planosDeSaude.setVisibility(View.GONE);

            }
            public void onNothingSelected(AdapterView<?> parent) {
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
        return id;
    }

    @Override
    public void flipper(boolean next) {

    }
}
