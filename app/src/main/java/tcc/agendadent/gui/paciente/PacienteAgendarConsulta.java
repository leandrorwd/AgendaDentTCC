package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import tcc.agendadent.R;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteAgendarConsulta extends LinearLayout implements ClassesPaciente{
    private Activity activity;
    private Button botaopesquisar;
    private int id;

    public PacienteAgendarConsulta(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_busca_dentista, this);
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        botaopesquisar = (Button) findViewById(R.id.PesquisarDentistas);
    }

    private void setEventos() {
        botaopesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaopesquisar();
            }
        });
    }

    private void botaopesquisar() {
        activity.startActivity(new Intent(activity, PacienteListarDentistas.class));
    }

    @Override
    public void onResume() {

    }

    @Override
    public boolean needResume() {
        return false;
    }

    @Override
    public int getIdMenu() {
        return id;
    }

    @Override
    public void flipper(boolean next) {

    }
}
