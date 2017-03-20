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
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.layout_auxiliares.TemplateConsultaAgenda;
import tcc.agendadent.gui.layout_auxiliares.Template_card_horario;
import tcc.agendadent.objetos.Agenda;
import tcc.agendadent.objetos.AgendaSub;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.DialogAux;

public class AgendaDiaria extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dentista);
        configuraMenu();
        buscaAgendaDiaria();
    }

    private void buscaAgendaDiaria() {
        DialogAux.dialogCarregandoSimples(AgendaDiaria.this);
        AgendaController.getInstance().getConsultasSemestre(DentistaController.getInstance().getDentistaLogado().getIdDentista()
        ,20171+"",AgendaDiaria.this,false);
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
