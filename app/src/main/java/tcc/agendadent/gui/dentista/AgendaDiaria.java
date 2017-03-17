package tcc.agendadent.gui.dentista;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.layout_auxiliares.TemplateConsultaAgenda;
import tcc.agendadent.gui.layout_auxiliares.Template_card_horario;
import tcc.agendadent.objetos.Agenda;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;

public class AgendaDiaria extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dentista);
        configuraMenu();
        teste();
    }

    private void teste() {
        DateTime d1 = new DateTime(2017, 3, 17, 8, 00);
        DateTime d2 = new DateTime(2017, 3, 17, 8, 30);
        DateTime d3 = new DateTime(2017, 3, 17, 13, 30);


        Consulta c1 = new Consulta(1, 1, d1.getMillis() , 0, "Particular","Nomezinho1");
        Consulta c2 = new Consulta(1, 1, d2.getMillis() , 0, "Particular","Nomezinho2");
        Consulta c3 = new Consulta(1, 1, d3.getMillis() , 0, "Particular","Nomezinho3");
        ArrayList<Consulta>  a1= new ArrayList<>();
        a1.add(c1);
        a1.add(c2);
        a1.add(c3);

        fakeWarever(a1);
    }

    private void fakeWarever(ArrayList<Consulta> consultasMarcadas) {
        //getConsultasFromPeriodo...
        LinearLayout horarioDiario = (LinearLayout) findViewById(R.id.consultasDiarias);
        Collections.sort(DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios());
        ArrayList<Horario> horarios= new ArrayList<>();
        int indexHoje = DateTime.now().dayOfWeek().get()-1;
        for(Horario h :DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios() ){
            if(h.getDiasSemana().get(indexHoje)){
                horarios.add(h);
            }
        }

        //Metodo que popula na tela a agenda do dia
        for(Horario horario : horarios){
            String horaInicial =horario.getHoraInicial();
            for(Consulta c1 : consultasMarcadas){
                DateTimeFormatter dateTimeFormatHora = DateTimeFormat.forPattern("HH:mm");
                String horaInicialConsulta = dateTimeFormatHora.print(c1.getDataFormat());
                if(horaInicial.equals(horaInicialConsulta)){
                    TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(AgendaDiaria.this,c1);
                    horarioDiario.addView(t1);
                }
                else{
                    TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(AgendaDiaria.this,horaInicial);
                    horarioDiario.addView(t1);
                }
                String[] parts =   horario.getDuracao().split(":");
                String[] parts2 =   horaInicial.split(":");

                int novaHora = (Integer.parseInt(parts[0]) + Integer.parseInt(parts2[0]));
                int novoMinuto = Integer.parseInt(parts[1]) + Integer.parseInt(parts2[1]);

                if(novoMinuto>=60){
                    novoMinuto = novoMinuto -60;
                    novaHora = novaHora+1;
                }
                String snovaHora = novaHora+"";
                String snovoMinuto = novoMinuto+"";
                if(novaHora<10){
                    snovaHora = "0" +novaHora ;
                }
                if(novoMinuto<10){
                    snovoMinuto = "0" +novoMinuto;
                }
                horaInicial = snovaHora +":"+snovoMinuto;
            }
        }
    }

    private void configuraMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.AgendaDiaria);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.Aguarde, R.string.Aguarde);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.config_agenda) {
            startActivity(new Intent(getApplicationContext(), ConfigAgenda.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.AgendaDiaria);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.AgendaDiaria);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
