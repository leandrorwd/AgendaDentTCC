package tcc.agendadent.gui.paciente;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.layout_auxiliares.TemplateCardDentista;
import tcc.agendadent.objetos.UsuarioDentista;


import tcc.agendadent.R;

public class PacienteVisualizaDentistas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_visualiza_dentistas);
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_paciente_visualiza_dentistas);
        layout.removeAllViews();
        for(UsuarioDentista dentista : PacienteController.getInstance().getDentistas()){
            TemplateCardDentista t1 = new TemplateCardDentista(PacienteVisualizaDentistas.this,dentista);
            layout.addView(t1);
        }

    }
}
