package tcc.agendadent.gui.paciente;

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
import android.widget.TextView;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.controllers.PacienteController;

public class Main_Paciente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static ArrayList<View> pilhaTelas;
    private static LinearLayout layoutMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_menu);
        pilhaTelas = new ArrayList<>();
        layoutMaster = (LinearLayout) findViewById(R.id.layoutPacienteMaster);
        configuraMenu();
        carregaBuscaDentista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((ClassesPaciente) getViewAtual()).needResume()) {
            ((ClassesPaciente) getViewAtual()).onResume();
        }
    }

    private void carregaBuscaDentista() {
        LinearLayout main = (LinearLayout) findViewById(R.id.layoutPacienteMaster);
//        PacienteAgendarConsulta layout = new PacienteAgendarConsulta(Main_Paciente.this, -1);
        PacienteConsultasAgendadas layout = new PacienteConsultasAgendadas(Main_Paciente.this, -1);
        main.addView(layout);
        pilhaTelas.add(layout);
    }

    private void configuraMenu() {
        setTitle("Próximas Consultas");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.BuscaDentista);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.Aguarde, R.string.Aguarde);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_paciente);
        View v = navigationView.getHeaderView(0);

        // set do nome e e-mail no navigationview

        TextView textNome = (TextView) v.findViewById(R.id.NomePaciente);
        TextView textEmail = (TextView) v.findViewById(R.id.emailPaciente);
        String nome = PacienteController.getInstance().getPacienteLogado().getNome().toString();
        if (!PacienteController.getInstance().getPacienteLogado().getSobrenome().toString().equals("null")) {
            nome += " " + PacienteController.getInstance().getPacienteLogado().getSobrenome().toString();
        }
        textNome.setText(nome);
        textEmail.setText(PacienteController.getInstance().getPacienteLogado().getEmail().toString());

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.BuscaDentista);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (pilhaTelas.size() == 1) {
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
                pilhaTelas.remove(pilhaTelas.get(pilhaTelas.size() - 1));
                layoutMaster.removeAllViews();
                layoutMaster.addView(pilhaTelas.get(pilhaTelas.size() - 1));
            }
        }).start();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.editar_perfil_paciente) {
            navegaJanelaPaciente(R.id.editar_perfil_paciente);
        }

        if (id == R.id.agendar_consulta_paciente) {
            navegaJanelaPaciente(R.id.agendar_consulta_paciente);
        }

        if (id == R.id.consultas_agendadas_paciente) {
            navegaJanelaPaciente(R.id.consultas_agendadas_paciente);
        }

        if (id == R.id.lista_espera_paciente) {
            navegaJanelaPaciente(R.id.lista_espera_paciente);
        }

        if (id == R.id.historico_consultas_paciente) {
            navegaJanelaPaciente(R.id.historico_consultas_paciente);
        }

        if (id == R.id.configuracoes_paciente) {
            navegaJanelaPaciente(R.id.configuracoes_paciente);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.BuscaDentista);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navegaJanelaPaciente(final int id_janela) {
            layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                @Override
                public void run() {
                    View view = null;
                    String titulo = "AgendaDent";
                    if (id_janela == R.id.editar_perfil_paciente) {
                        view = new PacienteEditarPerfil(Main_Paciente.this, id_janela);
                        titulo = "Editar Perfil";
                    }

                    if (id_janela == R.id.agendar_consulta_paciente) {
                        view = new PacienteAgendarConsulta(Main_Paciente.this, id_janela);
                        titulo = "Agendar Consultas";
                    }

                    if (id_janela == R.id.consultas_agendadas_paciente) {
                        view = new PacienteConsultasAgendadas(Main_Paciente.this, id_janela);
                        titulo = "Próximas Consultas";
                    }

                    if (id_janela == R.id.lista_espera_paciente) {
                        view = new PacienteListaEspera(Main_Paciente.this, id_janela);
                        titulo = "Lista de Espera";
                    }

                    if (id_janela == R.id.historico_consultas_paciente) {
                        view = new PacienteHistoricoConsultas(Main_Paciente.this, id_janela);
                        titulo = "Histórico de Consultas";
                    }

                    if (id_janela == R.id.configuracoes_paciente) {
                        view = new PacienteConfiguracoes(Main_Paciente.this, id_janela);
                        titulo = "Configurações";
                    }

                    layoutMaster.removeAllViews();
                    layoutMaster.addView(view);
                    setTitle(titulo);
                    layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                    pilhaTelas.add(view);

                }
            }).start();

    }

    public Object getViewAtual() {
//        DialogAux.dialogOkSimples(Main_Paciente.this, "pilhatelasSize", String.valueOf(pilhaTelas.size()));
        return pilhaTelas.get(pilhaTelas.size() - 1);
    }

    public static void navegaConsultaAgendadaMarcarConsulta(final Activity activity){
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.BuscaDentista);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                @Override
                public void run() {
                    layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                    layoutMaster.removeAllViews();
                    layoutMaster.animate().alpha(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            View view = null;
                            String titulo = "AgendaDent";
                            view = new PacienteAgendarConsulta(activity, R.id.agendar_consulta_paciente);
                            titulo = "Agendar Consultas";
                            layoutMaster.removeAllViews();
                            layoutMaster.addView(view);
                            activity.setTitle(titulo);
                            layoutMaster.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                            pilhaTelas.add(view);

                        }
                    }).start();
                }
            }).start();
        }
    }
}