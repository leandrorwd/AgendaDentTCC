package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteEditarPerfil extends LinearLayout implements ClassesPaciente {
    private Activity activity;
    private int id;

    public PacienteEditarPerfil(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_editar_perfil, this);
        exibeJanela();
        this.id = id_janela;
    }

    private void exibeJanela() {
    }

    @Override
    public void onResume() {
        exibeJanela();
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
