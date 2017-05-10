package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteHistoricoConsultas extends LinearLayout implements ClassesPaciente{

    private Activity activity;
    private int id;
    LinearLayout layout;

    public PacienteHistoricoConsultas(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_historico_consultas, this);
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
         layout = (LinearLayout) findViewById(R.id.layoutHistoricoConsultas);
    }

    private void setEventos() {
        DialogAux.dialogCarregandoSimples(activity);
        AgendaController.getInstance().getConsultasAgendadasBC(PacienteController.getInstance().getPacienteLogado().getIdPaciente(), activity, layout, false);
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
