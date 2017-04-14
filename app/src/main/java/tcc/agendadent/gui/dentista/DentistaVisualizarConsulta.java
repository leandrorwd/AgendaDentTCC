package tcc.agendadent.gui.dentista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Consulta;

public class DentistaVisualizarConsulta extends AppCompatActivity {
    private TextView textoNomePaciente;
    private TextView tipoConsulta;
    private TextView emailPaciente;
    private TextView telefonePaciente;
    private TextView dataConsulta;
    private TextView horaConsulta;
    private AppCompatButton botao;
    private Consulta consulta;
    private String userTipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dentista_visualiza_consulta);
        instanciaArtefatos();
        setEventos();
        Intent i = getIntent();
        consulta = (Consulta)i.getSerializableExtra("consulta");
        userTipo =(String)i.getSerializableExtra("user");
        carregaConsulta();


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarVisualizarConsulta);
//        setSupportActionBar(toolbar);
//
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }

    }

    private void carregaConsulta() {
        if(userTipo.equals("dentista")){
            String dia;
            String mes;
            String hora;
            String minuto;
            int horaAux =consulta.getDataFormat().getHourOfDay();
            try{
            String operador = consulta.getDataFormat().toString().substring(23,26);
            int valor = Integer.parseInt(consulta.getDataFormat().toString().substring(24,26));
                if(operador.contains("+")){
                    horaAux = horaAux-valor;
                }
                else{
                    horaAux = horaAux +valor;
                }
            }catch (Exception e){

            }


            if(consulta.getDataFormat().getDayOfMonth()>=9)
                dia = "" + consulta.getDataFormat().getDayOfMonth();
            else
                dia = "0" + consulta.getDataFormat().getDayOfMonth();

            if(consulta.getDataFormat().getMonthOfYear()>=9)
                mes = "" + consulta.getDataFormat().getMonthOfYear();
            else
                mes = "0" + consulta.getDataFormat().getMonthOfYear();

            if(horaAux>=9)
                hora = "" + horaAux;
            else
                hora = "0" + horaAux;

            if(consulta.getDataFormat().getMinuteOfHour()>=9)
                minuto = "0" + consulta.getDataFormat().getMinuteOfHour();
            else
                minuto = "0" + consulta.getDataFormat().getMinuteOfHour();



            dataConsulta.setText(dia +"/"
                    +mes +"/"
                    +consulta.getDataFormat().getYear());
            horaConsulta.setText(hora + ":"+ minuto);
            PacienteController.getInstance().getPacienteConsultaDen(consulta,false,null,DentistaVisualizarConsulta.this);

        }
        else{ //Paciente
            if (consulta == null) {

            }
            else{

            }
        }
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
