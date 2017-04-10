package tcc.agendadent.gui.paciente;

import android.app.Activity;
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

public class PacienteVisualizarHorarios extends LinearLayout {

    private Activity activity;
    private LinearLayout agendaDia;
    private static ViewFlipper flipper;
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    private static DateTime indiceSlider = DateTime.now();
    private static TextView header;
    private int id;

    public PacienteVisualizarHorarios(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_visualizar_horarios, this);
        listahorarios();
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void listahorarios() {
    }

    private void setEventos() {

    }

    private void instanciaArtefatos() {

    }
}
