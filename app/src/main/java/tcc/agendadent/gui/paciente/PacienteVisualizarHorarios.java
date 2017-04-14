package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.joda.time.DateTime;

import tcc.agendadent.R;

/**
 * Created by Leandro on 09/04/2017.
 */

public class PacienteVisualizarHorarios extends AppCompatActivity implements ActionMenuView.OnMenuItemClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_visualizar_horarios);
        instanciaArtefatos();
        setEventos();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarVisualizarHorarios));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void listahorarios() {
    }

    private void setEventos() {

    }

    private void instanciaArtefatos() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        finish();
        return true;
    }
}
