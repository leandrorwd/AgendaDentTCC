package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;


/**
 * Created by natha on 21/03/2017.
 */

public class AgendaCompleta extends LinearLayout  implements ClassesDentista, OnDateSelectedListener, OnMonthChangedListener {
    private Activity activity;
    private MaterialCalendarView calendario;
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
        calendario.setOnDateChangedListener(this);
        calendario.setOnMonthChangedListener(this);
    }

    private void instanciaArtefatos() {
        calendario = (MaterialCalendarView) findViewById(R.id.calendario);
        layoutConsultas = (LinearLayout) findViewById(R.id.layoutConsultas);

    }

    private void buscaAgenda() {
        DialogAux.dialogCarregandoSimples(activity);
        int mes = calendario.getCurrentDate().getMonth();
        String anoSemestre="";
        if(mes>=7){
            anoSemestre = calendario.getCurrentDate().getYear() +"A2";
        }
        else
            anoSemestre =  calendario.getCurrentDate().getYear() +"A1";
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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        DateTime d1 = new DateTime(date.getYear(),date.getMonth()+1,date.getDay(),12,12);
        AgendaController.getInstance().carregaAgendaData(activity,AgendaController.getInstance().getConsultasSemestre(),d1,R.id.layoutConsultas);
        Toast.makeText(activity,date.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
//        DateTime d1 = new DateTime(date);
    }
}

