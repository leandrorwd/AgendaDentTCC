package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;


/**
 * Created by natha on 21/03/2017.
 */

public class AgendaCompleta extends LinearLayout   {
    private Activity activity;

    public AgendaCompleta(Activity activity) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_agenda_completa, this);
        buscaAgenda();
    }

    private void buscaAgenda() {
    }

}

