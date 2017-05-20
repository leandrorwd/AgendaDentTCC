package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.dentista.DentistaAgendaDiaria;
import tcc.agendadent.gui.dentista.DentistaVisualizarConsulta;
import tcc.agendadent.gui.paciente.PacienteVisualizarConsulta;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class TemplatePacienteConsultasAgendadas extends RelativeLayout {

    private Activity tela;
    private Consulta consulta;
    private ArrayList<UsuarioDentista> dentistas;

    public TemplatePacienteConsultasAgendadas(Activity tela, Consulta consulta, ArrayList<UsuarioDentista> dentistasBC) {
        super(tela);
        this.tela = tela;
        this.consulta = consulta;
        this.dentistas = dentistasBC;
        View.inflate(tela, R.layout.template_paciente_consultas_agendadas, this);
        setEventos();
        preencherCard();
    }

    private void setEventos() {
        final CardView card = (CardView) findViewById(R.id.card_view);
        if (card != null) {
            card.setOnTouchListener(new OnSwipeTouchListener(tela) {
                public void onClick(MotionEvent event) {
                    Intent i = new Intent(tela, PacienteVisualizarConsulta.class);
                    if (consulta != null) {
                        i.putExtra("consulta", consulta);
                        i.putExtra("user", "paciente");
                        UsuarioDentista dentista = getDentista(consulta);
                        i.putExtra("dentista", getNomeCompletoDentista(dentista));
                        i.putExtra("email", dentista.getEmail());
                        i.putExtra("telefone", dentista.getTelefone());
                        tela.startActivity(i);
                    }
//                    else {
//                        dialogSuspender();
//                    }
                }
            });
        }
    }

    public UsuarioDentista getDentista(Consulta consulta) {
        int indiceDentista = 0;
        String idclassedentista, idarraydentistas;
        for (int i = 0; i < dentistas.size(); i++) {
            idclassedentista = Long.toString(consulta.getIdDentista());
            idarraydentistas = Long.toString(dentistas.get(i).getIdDentista());
            if (idclassedentista.equals(idarraydentistas)) {
                indiceDentista = i;
            }
        }
        return dentistas.get(indiceDentista);
    }

    public String getNomeCompletoDentista(UsuarioDentista dentista) {
        if (dentista.isMasculino()) {
            return "Dr. " + dentista.getNome() + " " + dentista.getSobreNome();
        } else {
            return "Dra. " + dentista.getNome() + " " + dentista.getSobreNome();
        }
    }

    public void preencherCard() {
        UsuarioDentista dentista = getDentista(consulta);

        TextView textoPacienteData = (TextView) findViewById(R.id.textoPacienteData);
        DateTimeFormatter formatData = DateTimeFormat.forPattern("dd/MM");
        DateTime data = new DateTime(consulta.getDataConsulta());
        textoPacienteData.setText(formatData.print(data));
        TextView textoPacienteHoraInicial = (TextView) findViewById(R.id.textoPacienteHoraInicial);
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
        textoPacienteHoraInicial.setText(formatHorario.print(horaCorrida));

        if (dentista.isMasculino()) {
            TextView textoNomeDentista = (TextView) findViewById(R.id.textoNomeDentista);
            textoNomeDentista.setText("Dr. " + dentista.getNome() + " " + dentista.getSobreNome());
        } else {
            TextView textoNomeDentista = (TextView) findViewById(R.id.textoNomeDentista);
            textoNomeDentista.setText("Dra. " + dentista.getNome() + " " + dentista.getSobreNome());
        }

        TextView textoEnderecoDentista = (TextView) findViewById(R.id.textoEnderecoDentista);
        textoEnderecoDentista.setText(dentista.getEndereco().getRuaNumero());

        TextView textoTipoConsulta = (TextView) findViewById(R.id.textoTipoConsulta);
        textoTipoConsulta.setText(consulta.getTipoConsulta());

        TextView textoDuracao = (TextView) findViewById(R.id.textoDuracao);
        DateTime datinha = new DateTime(consulta.getDuracao());
        try {
            String operador = datinha.toString().substring(23, 26);
            int valor = Integer.parseInt(datinha.toString().substring(24, 26));
            if (operador.contains("+")) {
                datinha = datinha.minusHours(valor);
            } else {
                datinha = datinha.plusHours(valor);
            }
        } catch (Exception e) {
           if(datinha.getYear()==1969){
              // datinha = datinha.plusHours(1);
           }
        }
        String duracao= "";
        if(datinha.getHourOfDay()>0){

            duracao = datinha.getHourOfDay() +":";

            if(datinha.getMinuteOfHour()<10){
                duracao = duracao + "0"+datinha.getMinuteOfHour() +"h";
            }
            else{
                duracao = duracao + datinha.getMinuteOfHour() +"h";
            }
        }
        else{
            if(datinha.getMinuteOfHour()<10){
                duracao =   "0"+datinha.getMinuteOfHour() +"min";
            }
            else{
                duracao =  +datinha.getMinuteOfHour() +"min";
            }
        }

        textoDuracao.setText(duracao);
    }
}
