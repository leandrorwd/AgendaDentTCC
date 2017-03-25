package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteHistoricoConsultas extends LinearLayout implements ClassesPaciente{

    private Activity activity;
    private int id;

    public PacienteHistoricoConsultas(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_historico_consultas, this);
        this.id = id_janela;
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
