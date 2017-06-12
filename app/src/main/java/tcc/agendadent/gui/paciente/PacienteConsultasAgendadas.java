package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.PacienteController;
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
    boolean firstTime;

    public PacienteConsultasAgendadas(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        firstTime = true;
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
                AgendaController.getInstance().getConsultasAgendadasBC(PacienteController.getInstance().getPacienteLogado().getIdPaciente(), activity, layout, true);
            }
        });

        DialogAux.dialogCarregandoSimples(activity);
        AgendaController.getInstance().getConsultasAgendadasBC(PacienteController.getInstance().getPacienteLogado().getIdPaciente(), activity, layout, true);
    }

    @Override
    public void onResume() {
        View.inflate(activity, R.layout.paciente_consultas_agendadas, this);
        instanciaArtefatos();
        setEventos();
    }

    @Override
    public boolean needResume() {
        return true;
    }

    @Override
    public int getIdMenu() {
        return id;
    }

    @Override
    public void flipper(boolean next) {
    }
}
