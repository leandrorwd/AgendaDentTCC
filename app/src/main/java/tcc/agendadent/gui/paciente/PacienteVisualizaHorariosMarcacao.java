package tcc.agendadent.gui.paciente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.dentista.Interface_Dentista;
import tcc.agendadent.servicos.DialogAux;

public class PacienteVisualizaHorariosMarcacao extends AppCompatActivity implements  OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView calendario;
    private LinearLayout layoutConsultas;
    private KillReceiver mKillReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_visualiza_horarios_marcacao);
        instanciaArtefatos();
        setEventos();
        buscaAgenda();
        mKillReceiver = new KillReceiver();
        registerReceiver(mKillReceiver, IntentFilter.create("kill", "text/plain"));

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mKillReceiver);
    }
    private final class KillReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setEventos() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarVisualizaHorariosDentista));
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        calendario.setOnDateChangedListener(this);
        calendario.setOnMonthChangedListener(this);
        calendario.setDateSelected(DateTime.now().toDate(),true);
    }

    private void instanciaArtefatos() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarVisualizaDentistas));
        setTitle("Agendar Consulta");
        calendario = (MaterialCalendarView) findViewById(R.id.calendario);
        layoutConsultas = (LinearLayout) findViewById(R.id.layoutConsultas);
    }

    private void buscaAgenda() {
        DialogAux.dialogCarregandoSimples(PacienteVisualizaHorariosMarcacao.this);
        int mes = calendario.getCurrentDate().getMonth();
        String anoSemestre="";
        if(mes>=7){
            anoSemestre = calendario.getCurrentDate().getYear() +"A2";
        }
        else
            anoSemestre =  calendario.getCurrentDate().getYear() +"A1";
        anoSemestre = anoSemestre.replace("A","");
        AgendaController.getInstance().getConsultasCompleto(PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getIdDentista()
                ,anoSemestre,PacienteVisualizaHorariosMarcacao.this,false);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        DateTime d1 = new DateTime(date.getYear(),date.getMonth()+1,date.getDay(),12,12);
        AgendaController.getInstance().carregaAgendaDataAgenda(PacienteVisualizaHorariosMarcacao.this,AgendaController.getInstance().getConsultasSemestre(),d1,R.id.layoutConsultas);
        AgendaController.getInstance().setMomento(date.getYear(),date.getMonth()+1,date.getDay(),12,12);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
