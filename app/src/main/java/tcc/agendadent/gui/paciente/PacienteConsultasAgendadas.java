package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.dentista.DentistaConfigAgendaAdiciona;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteConsultasAgendadas extends LinearLayout implements ClassesPaciente {
    private Activity activity;
    private int id;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    LinearLayout layout;

    public PacienteConsultasAgendadas(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_consultas_agendadas, this);
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        layout = (LinearLayout) findViewById(R.id.layoutConsultasAgendadas);
    }

    private void setEventos() {
        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Paciente.navegaConsultaAgendadaMarcarConsulta(activity);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeAllViews();
                DialogAux.dialogCarregandoSimples(activity);
                AgendaController.getInstance().getConsultasAgendadasBC(PacienteController.getInstance().getPacienteLogado().getIdPaciente(), "20171", activity, layout, true);
            }
        });

        DialogAux.dialogCarregandoSimples(activity);
        AgendaController.getInstance().getConsultasAgendadasBC(PacienteController.getInstance().getPacienteLogado().getIdPaciente(), "20171", activity, layout, true);
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
