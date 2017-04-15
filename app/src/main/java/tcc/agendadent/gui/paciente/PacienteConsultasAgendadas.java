package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

import static android.R.attr.button;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteConsultasAgendadas extends LinearLayout implements ClassesPaciente {
    private Activity activity;
    private int id;
    private FloatingActionButton botaoAdicionar;

    public PacienteConsultasAgendadas(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_consultas_agendadas, this);
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        botaoAdicionar = (FloatingActionButton) findViewById(R.id.botaoNovoAgendamentoConsulta);
    }

    private void setEventos() {
        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 botaoAdicionar();
            }
        });
    }

    private void botaoAdicionar() {


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
