package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

import tcc.agendadent.R;
import tcc.agendadent.objetos.Consulta;

public class TemplateConsultaAgenda extends RelativeLayout {

    private Activity tela;


    public TemplateConsultaAgenda(Activity tela, Consulta c1) {
        super(tela);
        this.tela=tela;
        View.inflate(tela, R.layout.activity_template_consulta_agenda, this);
        preencheHorario(c1);
    }
    public TemplateConsultaAgenda(Activity tela, String horaInicial) {
        super(tela);
        this.tela=tela;
        View.inflate(tela, R.layout.activity_template_consulta_agenda, this);
        horarioLivre(horaInicial);
    }

    private void horarioLivre(String horaInicial) {
        TextView textoAux = (TextView) findViewById(R.id.textNomePaciente);
        textoAux.setText("Livre");

        textoAux = (TextView) findViewById(R.id.textoHora);
        textoAux.setText(horaInicial);

        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setText("?????");
    }

    private void preencheHorario(Consulta c){
        TextView textoAux = (TextView) findViewById(R.id.textoHora);
        DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm");
        c.getDataFormat();
        textoAux.setText(format.print(c.getDataFormat()));

        textoAux = (TextView) findViewById(R.id.textNomePaciente);
        textoAux.setText(c.getNomePaciente());

        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setText(c.getTipoConsulta());

    }

}
