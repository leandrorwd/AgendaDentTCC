package tcc.agendadent.gui.dentista;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

public class Main_Dentista extends  AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<View> pilhaTelas;
    private LinearLayout layoutMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dentista);
        pilhaTelas = new ArrayList<>();
        layoutMaster = (LinearLayout) findViewById(R.id.layoutDentistaMaster);
        configuraMenu();
        carregaAgendaHoje();
    }


    private void carregaAgendaHoje() {
        LinearLayout main =(LinearLayout)findViewById(R.id.layoutDentistaMaster);
        AgendaDiaria layout = new AgendaDiaria(Main_Dentista.this);
        main.addView(layout);
        pilhaTelas.add(layout);
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
            navegaJanela(R.id.config_agenda);
        }
        if (id == R.id.visualizar_agenda_full) {
            navegaJanela(R.id.visualizar_agenda_full);
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
            if(pilhaTelas.size()==1){
                super.onBackPressed();
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
                layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                pilhaTelas.remove(pilhaTelas.get(pilhaTelas.size()-1));
                layoutMaster.removeAllViews();
                layoutMaster.addView(pilhaTelas.get(pilhaTelas.size()-1));
            }
        }).start();
    }

    private void navegaJanela(final int id_janela) {
        layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                View view =null;
                if(id_janela== R.id.config_agenda)
                    view = new ConfigAgenda(Main_Dentista.this);
                if(id_janela== R.id.visualizar_agenda_full)
                    view = new AgendaCompleta(Main_Dentista.this);

                layoutMaster.removeAllViews();
                layoutMaster.addView(view);
                layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                pilhaTelas.add(view);
            }
        }).start();
    }

    public View getViewAtual(){
        return pilhaTelas.get(pilhaTelas.size()-1);
    }

}
