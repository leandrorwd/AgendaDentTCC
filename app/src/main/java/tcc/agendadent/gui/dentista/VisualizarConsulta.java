package tcc.agendadent.gui.dentista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import tcc.agendadent.R;

public class VisualizarConsulta extends AppCompatActivity {
    private TextView textoNomePaciente;
    private TextView tipoConsulta;
    private TextView emailPaciente;
    private TextView telefonePaciente;
    private TextView dataConsulta;
    private TextView horaConsulta;
    private AppCompatButton botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_consulta);
        instanciaArtefatos();
        setEventos();
    }

    private void setEventos() {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void instanciaArtefatos() {
        textoNomePaciente = (TextView) findViewById(R.id.textNomePaciente);
        tipoConsulta = (TextView) findViewById(R.id.tipoConsulta);
        emailPaciente = (TextView) findViewById(R.id.emailPaciente);
        telefonePaciente = (TextView) findViewById(R.id.telefonePaciente);
        textoNomePaciente = (TextView) findViewById(R.id.textNomePaciente);
        dataConsulta = (TextView) findViewById(R.id.dataConsulta);
        horaConsulta = (TextView) findViewById(R.id.horaConsulta);
        botao = (AppCompatButton) findViewById(R.id.botaoDesmarcarDentista);
    }
}
