package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;


/**
 * Created by natha on 21/03/2017.
 */

public class AgendaCompleta extends LinearLayout  implements ClassesDentista {
    private Activity activity;

    public AgendaCompleta(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_agenda_completa, this);
        buscaAgenda();
        this.id = id_janela;

    }

    private void buscaAgenda() {
    }

    @Override
    public void onResume() {
        buscaAgenda();
    }

    @Override
    public boolean needResume() {
        return false;
    }
    private int id;
    @Override
    public int getIdMenu(){
        return id;
    }

    @Override
    public void flipper(boolean next) {

    }
}

