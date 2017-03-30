package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.icu.util.Calendar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;


/**
 * Created by natha on 21/03/2017.
 */

public class AgendaCompleta extends LinearLayout  implements ClassesDentista {
    private Activity activity;
    private CalendarView calendario;
    private LinearLayout layoutConsultas;
    public AgendaCompleta(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_agenda_completa, this);
        instanciaArtefatos();
        setEventos();
        buscaAgenda();
        this.id = id_janela;

    }

    private void setEventos() {
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int ano, int mes, int dia) {
                mes++;
                Toast.makeText(activity,ano+"/"+mes+"/"+dia,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void instanciaArtefatos() {
        calendario = (CalendarView) findViewById(R.id.calendario);
       layoutConsultas = (LinearLayout) findViewById(R.id.layoutConsultas);
    }

    private void buscaAgenda() {
        DialogAux.dialogCarregandoSimples(activity);
        DateTime data = new DateTime(calendario.getDate());
        int mes = data.monthOfYear().get();
        String anoSemestre= data.year().get()+"";
        if(mes>=7){
            anoSemestre = data.year().get() +"A2";
        }
        else
            anoSemestre = data.year().get() +"A1";
        anoSemestre = anoSemestre.replace("A","");
        AgendaController.getInstance().getConsultasCompleto(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                ,anoSemestre,activity,false);
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

