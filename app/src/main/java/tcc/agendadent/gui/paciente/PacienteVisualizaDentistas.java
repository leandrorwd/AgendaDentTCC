package tcc.agendadent.gui.paciente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.layout_auxiliares.TemplateCardDentista;
import tcc.agendadent.objetos.UsuarioDentista;


import tcc.agendadent.R;

public class PacienteVisualizaDentistas extends AppCompatActivity {
    private KillReceiver mKillReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_visualiza_dentistas);
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_paciente_visualiza_dentistas);
        layout.removeAllViews();
        for (UsuarioDentista dentista : PacienteController.getInstance().getDentistas()) {
            System.out.println("teste");
            TemplateCardDentista t1 = new TemplateCardDentista(PacienteVisualizaDentistas.this, dentista);
            layout.addView(t1);
        }
        instanciaArtefatos();
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

    private void instanciaArtefatos() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarVisualizaDentistas));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle("Agendar Consulta");
    }


}
