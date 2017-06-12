package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteListaEspera extends LinearLayout implements ClassesPaciente{

    private Activity activity;
    private int id;

    public PacienteListaEspera(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_lista_espera, this);
        this.id = id_janela;
        DialogAux.dialogCarregandoSimples(activity);
        PacienteController.getInstance().getListaEspera(activity);
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
