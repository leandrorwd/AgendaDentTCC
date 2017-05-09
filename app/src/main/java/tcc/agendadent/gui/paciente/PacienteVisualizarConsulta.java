package tcc.agendadent.gui.paciente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.AgendaBC;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.servicos.DialogAux;

public class PacienteVisualizarConsulta extends AppCompatActivity {
    private TextView textoNomeDentista;
    private TextView tipoConsulta;
    private TextView emailDent;
    private TextView telefoneDent;
    private TextView dataConsulta;
    private TextView horaConsulta;
    private TextView duracaoConsulta;
    private AppCompatButton botaoDesmarcarConsulta;
    private Consulta consulta;
    private String userTipo;
    private String nomeDentista;
    private String emailDentista;
    private String telefoneDentista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_visualiza_consulta);
        instanciaArtefatos();
        setEventos();
        Intent i = getIntent();
        consulta = (Consulta) i.getSerializableExtra("consulta");
        userTipo = (String) i.getSerializableExtra("user");
        nomeDentista = (String) i.getSerializableExtra("dentista");
        emailDentista = (String) i.getSerializableExtra("email");
        telefoneDentista = (String) i.getSerializableExtra("telefone");

        carregaConsulta();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void carregaConsulta() {
        if (userTipo.equals("paciente")) {

            textoNomeDentista.setText(nomeDentista);
            emailDent.setText(emailDentista);
            telefoneDent.setText(telefoneDentista);
            tipoConsulta.setText(consulta.getTipoConsulta());

            String dia;
            String mes;
//            String hora;
//            String minuto;
//            int horaAux = consulta.getDataFormat().getHourOfDay();
//            try {
//                String operador = consulta.getDataFormat().toString().substring(23, 26);
//                int valor = Integer.parseInt(consulta.getDataFormat().toString().substring(24, 26));
//                if (operador.contains("+")) {
//                    horaAux = horaAux - valor;
//                } else {
//                    horaAux = horaAux + valor;
//                }
//            } catch (Exception e) {
//
//            }

            if (consulta.getDataFormat().getDayOfMonth() >= 9)
                dia = "" + consulta.getDataFormat().getDayOfMonth();
            else
                dia = "0" + consulta.getDataFormat().getDayOfMonth();

            if (consulta.getDataFormat().getMonthOfYear() >= 9)
                mes = "" + consulta.getDataFormat().getMonthOfYear();
            else
                mes = "0" + consulta.getDataFormat().getMonthOfYear();

            dataConsulta.setText(dia + "/"
                    + mes + "/"
                    + consulta.getDataFormat().getYear());

            DateTimeFormatter formatHorario = DateTimeFormat.forPattern("HH:mm");
            DateTime hora = new DateTime(consulta.getDataConsulta());
            DateTime aux = DateTime.now();
            DateTime horaCorrida;
            try {
                String operador = aux.toString().substring(23, 26);
                int valor = Integer.parseInt(aux.toString().substring(24, 26));
                if (operador.contains("+")) {
                    horaCorrida = hora.minusHours(valor);
                } else {
                    horaCorrida = hora.plusHours(valor);
                }
            } catch (Exception e) {
                horaCorrida = hora;
            }
            horaConsulta.setText(formatHorario.print(horaCorrida));

            Date date = new Date(consulta.getDuracao());
            Format formato = new SimpleDateFormat("mm");
            duracaoConsulta.setText(formato.format(date) + " minutos");

        } else { //Paciente
            if (consulta == null) {

            } else {

            }
        }
    }

    private void setEventos() {
        botaoDesmarcarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAux.dialogCarregandoSimples(PacienteVisualizarConsulta.this);
                AgendaController.getInstance().desmarcarConsulta(PacienteVisualizarConsulta.this, consulta);

            }
        });
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarVisualizarConsultaDetalhes));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void instanciaArtefatos() {
        textoNomeDentista = (TextView) findViewById(R.id.textNomeDentista);
        tipoConsulta = (TextView) findViewById(R.id.tipoConsulta);
        emailDent = (TextView) findViewById(R.id.emailDentista);
        telefoneDent = (TextView) findViewById(R.id.telefoneDentista);
        dataConsulta = (TextView) findViewById(R.id.dataConsulta);
        horaConsulta = (TextView) findViewById(R.id.horaConsulta);
        duracaoConsulta = (TextView) findViewById(R.id.duracaoConsulta);
        botaoDesmarcarConsulta = (AppCompatButton) findViewById(R.id.botaoDesmarcarDentista);

        setTitle("Visualizar Consulta");
    }
}
