package tcc.agendadent.gui.dentista;

import android.app.Activity;

import android.view.View;
import android.widget.LinearLayout;
import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;

public class AgendaDiaria extends LinearLayout    {
    private Activity activity;

    public AgendaDiaria(Activity activity) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_agenda_diaria_conteudo, this);
        buscaAgendaDiaria();
    }

    private void buscaAgendaDiaria() {
        DialogAux.dialogCarregandoSimples(activity);
        AgendaController.getInstance().getConsultasSemestre(DentistaController.getInstance().getDentistaLogado().getIdDentista()
        ,20171+"",activity,false);
    }


}
