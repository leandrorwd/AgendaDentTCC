package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.w3c.dom.Text;

import tcc.agendadent.R;
import tcc.agendadent.gui.dentista.AgendaDiaria;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class TemplateConsultaAgenda extends RelativeLayout {

    private Activity tela;


    public TemplateConsultaAgenda(Activity tela, Consulta c1) {
        super(tela);
        this.tela=tela;
        View.inflate(tela, R.layout.activity_template_consulta_agenda, this);
        setEventos();
        preencheHorario(c1);
    }
    public TemplateConsultaAgenda(Activity tela, String horaInicial) {
        super(tela);
        this.tela=tela;
        View.inflate(tela, R.layout.activity_template_consulta_agenda, this);
        setEventos();
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

        DateTime data = new DateTime(c.getDataConsulta());
        DateTime dataAux = data.withZone(DateTimeZone.UTC);
        LocalDateTime dataSemFuso = dataAux.toLocalDateTime();

        textoAux.setText(format.print(dataSemFuso));

        textoAux = (TextView) findViewById(R.id.textNomePaciente);
        textoAux.setText(c.getNomePaciente());

        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setText(c.getTipoConsulta());


    }
    private void setEventos() {
        final CardView card = (CardView) findViewById(R.id.card_view);
        if(card!=null) {
            card.setOnTouchListener(new OnSwipeTouchListener(tela) {

                public void onSwipeRight() {
                    AgendaDiaria.flipperHelper(tela,true);
                }

                public void onSwipeLeft() {
                    AgendaDiaria.flipperHelper(tela,false);
                }

                public void onClick(MotionEvent event) {
                    float x = event.getX() + card.getLeft();
                    float y = event.getY() + card.getTop();

                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        card.drawableHotspotChanged(x, y);
                    }

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            card.setPressed(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            card.setPressed(true);
                            break;
                    }
                }

            });
        }
    }
}
