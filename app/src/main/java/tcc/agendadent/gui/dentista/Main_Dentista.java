package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import tcc.agendadent.gui.paciente.ClassesPaciente;
import org.joda.time.DateTime;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;

public class Main_Dentista extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static ArrayList<View> pilhaTelas;
    private LinearLayout layoutMaster;
    private int idUltimaJanela;
    private static Activity activity;
    private boolean firstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dentista_menu);
        activity = Main_Dentista.this;
        pilhaTelas = new ArrayList<>();
        layoutMaster = (LinearLayout) findViewById(R.id.layoutDentistaMaster);
        DentistaController.getInstance().setMainClass(this);
        configuraMenu();
        carregaAgendaHoje();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeAux();
    }

    public void onResumeAux(){
        if (((Interface_Dentista) getViewAtual()).needResume()) {
           int i= ((Interface_Dentista) getViewAtual()).getIdMenu();
            ((Interface_Dentista) getViewAtual()).onResume();
        }
    }

    private void carregaAgendaHoje() {
        LinearLayout main = (LinearLayout) findViewById(R.id.layoutDentistaMaster);
        DentistaAgendaDiaria layout = new DentistaAgendaDiaria(Main_Dentista.this, -1);
        setTitle(setTitulo(layout));
        main.addView(layout);
        pilhaTelas.add(layout);
    }

    private void configuraMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDentista);
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
            navegaJanela(R.id.config_agenda);
        }
        if (id == R.id.visualizar_agenda_full) {
            navegaJanela(R.id.visualizar_agenda_full);
        }
        if (id == R.id.agenda_diaria) {
            if(firstTime)
                return true;
            navegaJanela(R.id.agenda_diaria);
        }
        if (id == R.id.editarPerfil) {
            navegaJanela(R.id.editarPerfil);
        }
        if (id == R.id.editarEspec) {
            navegaJanela(R.id.editarEspec);
        }
        if (id == R.id.editarConvenios) {
            navegaJanela(R.id.editarConvenios);
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
            if (pilhaTelas.size() == 1) {
                super.onBackPressed();
                DentistaAgendaDiaria.indiceSlider = DateTime.now();
                finish();
                return;
            }
            animacaoTrocaJanelaVolta();
        }
    }

    private void animacaoTrocaJanelaVolta() {
        layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {

                if (((Interface_Dentista) getViewAtual()).getIdMenu() == R.id.config_agenda)
                    DentistaController.getInstance().getHorariosTemporarios().clear();
                layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                pilhaTelas.remove(pilhaTelas.get(pilhaTelas.size() - 1));
                layoutMaster.removeAllViews();
                layoutMaster.addView(pilhaTelas.get(pilhaTelas.size() - 1));
                setTitle(setTitulo(pilhaTelas.get(pilhaTelas.size() - 1)));
                onResume();
            }
        }).start();
    }

    public static void animacaoTrocaJanelaVoltaExterno() {
        final LinearLayout layoutMaster = (LinearLayout) activity.findViewById(R.id.layoutDentistaMaster);
        layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (((Interface_Dentista) getViewAtual()).getIdMenu() == R.id.config_agenda)
                    DentistaController.getInstance().getHorariosTemporarios().clear();
                layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                pilhaTelas.remove(pilhaTelas.get(pilhaTelas.size() - 1));
                layoutMaster.removeAllViews();
                layoutMaster.addView(pilhaTelas.get(pilhaTelas.size() - 1));
                activity.setTitle(setTitulo(pilhaTelas.get(pilhaTelas.size() - 1)));
            }
        }).start();
    }

    private void navegaJanela(final int id_janela) {
        firstTime = false;
        if (((Interface_Dentista) getViewAtual()).getIdMenu() == id_janela) return;
        layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                View view = null;
                if (DentistaController.getInstance().getHorariosTemporarios() != null)
                    DentistaController.getInstance().getHorariosTemporarios().clear();
                View aux =null;
                for(View v : pilhaTelas){
                    if(((Interface_Dentista)v).getIdMenu()==id_janela)
                    {
                        aux = v;
                    }
                }
                if(aux!=null)
                    pilhaTelas.remove(aux);
                if (id_janela == R.id.config_agenda)
                    view = new DentistaConfigAgenda(Main_Dentista.this, id_janela);
                if (id_janela == R.id.visualizar_agenda_full)
                    view = new DentistaAgendaCompleta(Main_Dentista.this, id_janela);
                if (id_janela == R.id.agenda_diaria)
                    view = new DentistaAgendaDiaria(Main_Dentista.this, id_janela);
                if (id_janela == R.id.editarPerfil)
                    view = new DentistaPerfil(Main_Dentista.this, id_janela);
                if (id_janela == R.id.editarEspec)
                    view = new DentistaEspecializacoes(Main_Dentista.this, id_janela);
                if (id_janela == R.id.editarConvenios)
                    view = new DentistaConvenios(Main_Dentista.this, id_janela);
                layoutMaster.removeAllViews();
                layoutMaster.addView(view);
                activity.setTitle(setTitulo(view));
                layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                pilhaTelas.add(view);
            }
        }).start();
    }

    public static View getViewAtual() {
        return pilhaTelas.get(pilhaTelas.size() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((Interface_Dentista) getViewAtual()).onActivityResult(requestCode, resultCode, data);
    }

    private static String setTitulo(View view) {
        if (view.getClass().getSimpleName().equals("DentistaConfigAgenda")) {
            return "Configurar Horários";
        }
        if (view.getClass().getSimpleName().equals("DentistaAgendaCompleta")) {
            return "Agenda Completa";
        }
        if (view.getClass().getSimpleName().equals("DentistaAgendaDiaria")) {
            return "Agenda Diária";
        }
        if (view.getClass().getSimpleName().equals("DentistaPerfil")) {
            return "Editar Perfil";
        }
        if (view.getClass().getSimpleName().equals("DentistaEspecializacoes")) {
            return "Editar Especializações";
        }
        if (view.getClass().getSimpleName().equals("DentistaConvenios")) {
            return "Editar Convênios";
        }

        return "ConfigSetTitulo";
    }
}
