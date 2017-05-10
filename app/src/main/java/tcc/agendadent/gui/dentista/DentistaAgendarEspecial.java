package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;

/**
 * Created by natha on 09/05/2017.
 */

public class DentistaAgendarEspecial extends LinearLayout implements Interface_Dentista{
    private Activity activity;
    private LinearLayout principal;
    private Spinner spinnerHorarios;
    private RadioButton radioSim;
    private RadioButton radioNao;
    private EditText emailSim;
    private EditText emailNao;
    private EditText nomePaciente;
    private EditText celular;
    private CardView sim;
    private CardView nao;
    private Button butt;
    private int id;

    public DentistaAgendarEspecial(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        this.id = id_janela;
        View.inflate(activity, R.layout.dentista_agenda_especial, this);
        instanciaArtefatos();
        setEventos();
    }
    private void instanciaArtefatos() {
        spinnerHorarios = (Spinner) findViewById(R.id.spinnerHorarios);
        radioNao = (RadioButton) findViewById(R.id.radioNao);
        radioSim = (RadioButton) findViewById(R.id.radioSim);
        emailSim = (EditText) findViewById(R.id.emailPaciente);
        emailNao = (EditText) findViewById(R.id.emailPacienteNao);
        nomePaciente = (EditText) findViewById(R.id.nomePaciente);
        celular = (EditText) findViewById(R.id.celular);
        sim = (CardView) findViewById(R.id.cardviewSim);
        nao = (CardView) findViewById(R.id.cardviewnao);
        butt = (Button) findViewById(R.id.botaoAbreAgenda);


    }
    private void setEventos() {
        RadioGroup r1 = (RadioGroup) findViewById(R.id.radioGroup);
        r1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isChecked = ((RadioButton) group.findViewById(checkedId)).isChecked();
                if (isChecked && checkedId == R.id.radioSim) {
                    sim.setVisibility(View.VISIBLE);
                    nao.setVisibility(View.GONE);
                }
                if (isChecked && checkedId == R.id.radioNao) {
                    nao.setVisibility(View.VISIBLE);
                    sim.setVisibility(View.GONE);
                }
            }
        });

        butt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioSim.isChecked()){
                    AgendaController.getInstance().getPacienteViaMarcacaoConsulta
                            (emailSim.getText().toString(),activity,spinnerHorarios.getSelectedItemPosition());
                    //setar no controler,abrir agenda especial,marcar consulta
                }
                else{

                }
            }
        });
        radioSim.setChecked(true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
