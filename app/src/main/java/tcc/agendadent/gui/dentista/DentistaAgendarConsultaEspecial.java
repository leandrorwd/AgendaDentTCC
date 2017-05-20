package tcc.agendadent.gui.dentista;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.joda.time.DateTime;

import java.util.Calendar;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;

public class DentistaAgendarConsultaEspecial extends AppCompatActivity implements  OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView calendario;
    private LinearLayout layoutConsultas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentista_agendar_consulta_especial);
        instanciaArtefatos();
        setEventos();
        buscaAgenda();
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
        DialogAux.dialogCarregandoSimples(DentistaAgendarConsultaEspecial.this);
        int mes = calendario.getCurrentDate().getMonth();
        String anoSemestre="";
        if(mes>=7){
            anoSemestre = calendario.getCurrentDate().getYear() +"A2";
        }
        else
            anoSemestre =  calendario.getCurrentDate().getYear() +"A1";
        calendario.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1))
                .setMaximumDate(CalendarDay.from(2018, 7, 7))
                .commit();
        calendario.setSelectedDate(DateTime.now().toDate());
        anoSemestre = anoSemestre.replace("A","");
        AgendaController.getInstance().getConsultasCompleto(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                ,anoSemestre,DentistaAgendarConsultaEspecial.this,false);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        DateTime d1 = new DateTime(date.getYear(),date.getMonth()+1,date.getDay(),12,12);
        AgendaController.getInstance().carregaAgendaData(DentistaAgendarConsultaEspecial.this,AgendaController.getInstance().getConsultasSemestre(),d1,R.id.layoutConsultas);
        AgendaController.getInstance().setMomento(date.getYear(),date.getMonth()+1,date.getDay(),12,12);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
//        DateTime d1 = new DateTime(date);
    }
}
