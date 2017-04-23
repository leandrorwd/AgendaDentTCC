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

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.dentista.DentistaAgendaDiaria;
import tcc.agendadent.gui.dentista.DentistaVisualizarConsulta;
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
        this.tela=tela;
        this.consulta = consulta;
        this.dentistas = dentistasBC;
        View.inflate(tela, R.layout.template_paciente_consultas_agendadas, this);
        setEventos();
        preencherCard();
    }

    private void setEventos() {
    }

    public void preencherCard() {
        int idDentista = (int) consulta.getIdDentista();

        TextView textoPacienteData = (TextView) findViewById(R.id.textoPacienteData);
        DateTimeFormatter formatData = DateTimeFormat.forPattern("dd/MM");
        DateTime data = new DateTime(consulta.getDataConsulta());
        textoPacienteData.setText(formatData.print(data));




        TextView textoPacienteHoraInicial = (TextView) findViewById(R.id.textoPacienteHoraInicial);
        DateTimeFormatter formatHorario = DateTimeFormat.forPattern("HH:mm");
        DateTime hora = new DateTime(consulta.getDataConsulta());
        textoPacienteHoraInicial.setText(formatHorario.print(hora));





        TextView textoNomeDentista = (TextView) findViewById(R.id.textoNomeDentista);
        textoNomeDentista.setText(dentistas.get(idDentista-1).getNome() + " " + dentistas.get(idDentista-1).getSobreNome());

        TextView textoEnderecoDentista = (TextView) findViewById(R.id.textoEnderecoDentista);
        textoEnderecoDentista.setText(dentistas.get(idDentista-1).getEndereco().getRuaNumero());

        TextView textoTipoConsulta = (TextView) findViewById(R.id.textoTipoConsulta);
        textoTipoConsulta.setText(consulta.getTipoConsulta());

    }
}
