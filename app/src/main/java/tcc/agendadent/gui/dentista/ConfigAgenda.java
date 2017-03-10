package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.layout_auxiliares.Template_card_horario;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

public class ConfigAgenda extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton botaoAdd;
    FloatingActionButton botaoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_agenda);
        configuraMenu();
        instanciaArtefatos();
        setEventos();
    }


    public void onResume(){
        super.onResume();
        if(!ValidationTest.verificaInternet(ConfigAgenda.this)){
            dialogOkSimples(ConfigAgenda.this,"Erro",ConfigAgenda.this.getResources().getString(R.string.internetSemConexao));
            return;
        }
        carregaHorarios();
    }

    private void carregaHorarios() {
        DentistaController.getInstance().carregaHorarios(ConfigAgenda.this,DentistaController.getInstance().getDentistaLogado(),false);
    }

    private void setEventos() {
        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConfigAgendaAdiciona.class));

            }
        });
        botaoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DentistaController.getInstance().setDentista(ConfigAgenda.this,DentistaController.getInstance().getDentistaLogado(),false);
            }
        });
    }

    private void instanciaArtefatos() {
        botaoSave =(FloatingActionButton) findViewById(R.id.botaoSave);
        botaoAdd = (FloatingActionButton) findViewById(R.id.botaoAdd);
    }

    private void configuraMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.config_agenda1);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.config_agenda1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.config_agenda1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            DentistaController.getInstance().getAgenda().getHorarios().removeAll(DentistaController.getInstance().getHorariosTemporarios());
            DentistaController.getInstance().getHorariosTemporarios().clear();
            super.onBackPressed();

        }
    }


}
