package tcc.agendadent.gui.paciente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import tcc.agendadent.R;

/**
 * Created by Leandro on 09/04/2017.
 */

public class PacienteListarDentistas extends AppCompatActivity implements ActionMenuView.OnMenuItemClickListener {

    private Button botaoselecionardentistas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_lista_dentistas);
        instanciaArtefatos();
        setEventos();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListaDentistas);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setEventos() {
        botaoselecionardentistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoselecionardentistas();
            }
        });
    }

    private void instanciaArtefatos() {
        botaoselecionardentistas = (Button) findViewById(R.id.SelecionarDentista);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void botaoselecionardentistas() {
        Intent intent = new Intent(PacienteListarDentistas.this, PacienteVisualizarHorarios.class);
        startActivity(intent);
    }
}
