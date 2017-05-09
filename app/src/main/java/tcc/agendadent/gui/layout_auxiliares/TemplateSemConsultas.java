package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tcc.agendadent.R;
import tcc.agendadent.gui.paciente.PacienteVisualizarConsulta;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class TemplateSemConsultas extends RelativeLayout {

    private Activity tela;
    private Consulta consulta;
    private ArrayList<UsuarioDentista> dentistas;

    public TemplateSemConsultas(Activity tela) {
        super(tela);
        this.tela = tela;
        View.inflate(tela, R.layout.template_sem_consultas, this);
    }
}