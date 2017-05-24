package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.app.AlertDialog;
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

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.EventosController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.paciente.PacienteMarcaConsulta;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.OnSwipeTouchListener;

/**
 * Created by Work on 22/04/2017.
 */

public class TemplateVisualizaMarcacaoConsultaPaciente extends RelativeLayout {
    private Activity tela;
    private Consulta consulta;
    private String usuarioTipo;
    private String horaInicial;
    private Horario horario;

    public TemplateVisualizaMarcacaoConsultaPaciente(Activity tela, Consulta c1,String usuario,String duracao) {
        super(tela);
        this.tela=tela;
        usuarioTipo=usuario;
        View.inflate(tela, R.layout.template_consulta_agenda_marcacao, this);
        preencheHorario(c1);
        consulta = c1;
        setEventosNegado(true);

    }
    public TemplateVisualizaMarcacaoConsultaPaciente(Activity tela, String horaInicial,
                                                     String usuario, String duracao, Horario horario) {
        super(tela);
        this.tela=tela;
        usuarioTipo=usuario;
        View.inflate(tela, R.layout.template_consulta_agenda_marcacao, this);
        this.horaInicial = horaInicial;
        this.horario=horario;
        horarioLivre(horaInicial);
    }

    private void horarioLivre(String horaInicial) {
        TextView textoAux = (TextView) findViewById(R.id.textoHora);
        textoAux.setText(horaInicial);
        if(PacienteController.getInstance().getTipoConsulta().equals("Particular")
                && horario.isParticular()){
            textoAux = (TextView) findViewById(R.id.textTipoConsulta);
            textoAux.setText(tela.getResources().getString(R.string.livre));
            CardView c1 = (CardView) findViewById(R.id.card_view);
            c1.setCardBackgroundColor(getResources().getColor(R.color.livre));
            setEventos();


        }
        else if(PacienteController.getInstance().getTipoConsulta().equals("SUS")
                && horario.isSus()){
            textoAux = (TextView) findViewById(R.id.textTipoConsulta);
            textoAux.setText(tela.getResources().getString(R.string.livre));
            CardView c1 = (CardView) findViewById(R.id.card_view);
            c1.setCardBackgroundColor(getResources().getColor(R.color.livre));
            setEventos();


        }
        else if(PacienteController.getInstance().getTipoConsulta().equals("Convênio")
                && horario.isConvenio()){
            textoAux = (TextView) findViewById(R.id.textTipoConsulta);
            textoAux.setText(tela.getResources().getString(R.string.livre));
            CardView c1 = (CardView) findViewById(R.id.card_view);
            c1.setCardBackgroundColor(getResources().getColor(R.color.livre));
            setEventos();

        }
        else{
            textoAux = (TextView) findViewById(R.id.textTipoConsulta);
            textoAux.setText( tela.getResources().getString(R.string.indisponivell));
            textoAux.setTextSize(18);
            textoAux.setPadding(50,50,0,0);
            CardView c1 = (CardView) findViewById(R.id.card_view);
            c1.setCardBackgroundColor(getResources().getColor(R.color.ocupado));
            setEventosNegado(false);
        }
    }

    private void setEventosNegado(boolean ocupado) {
        if(ocupado){
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(tela)
                            .setTitle("Aviso")
                            .setMessage("Você deseja ser notificado caso está consulta seja cancelada?")
                            .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    EventosController.getInstance().getKeyConsulta(consulta,tela);
                                    //EventosController.getInstance().esperaConsulta(consulta);
                                }
                            })
                            .setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            }).show();

                }
            });


        }
    }

    private void preencheHorario(Consulta c) {
        TextView textoAux = (TextView) findViewById(R.id.textoHora);
        DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm");
        DateTime data = new DateTime(c.getDataConsulta());
        DateTime dataAux = data.withZone(DateTimeZone.UTC);
        LocalDateTime dataSemFuso = dataAux.toLocalDateTime();
        textoAux.setText(format.print(dataSemFuso));
        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setText( tela.getResources().getString(R.string.ocupado));
        CardView c1 = (CardView) findViewById(R.id.card_view);
        c1.setCardBackgroundColor(getResources().getColor(R.color.ocupado));
    }
    private void setEventos() {
        final CardView card = (CardView) findViewById(R.id.card_view);
        if(card!=null) {
            card.setOnTouchListener(new OnSwipeTouchListener(tela) {

                public void onSwipeRight() {

                }

                public void onSwipeLeft() {

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
                    if(AgendaController.getInstance().getDataAux().getYear() <= DateTime.now().getYear()){
                        if(AgendaController.getInstance().getDataAux().getDayOfYear() <= DateTime.now().getDayOfYear()){
                            if(AgendaController.getInstance().getDataAux().getDayOfYear() == DateTime.now().getDayOfYear()){
                                if(Integer.parseInt(horaInicial.substring(0,2))<=DateTime.now().getHourOfDay()){
                                    DialogAux.dialogOkSimples(tela,tela.getString(R.string.erro),tela.getString(R.string.erroMarcarConsultaPassado));
                                    return;
                                }
                            }
                            else{
                                DialogAux.dialogOkSimples(tela,tela.getString(R.string.erro),tela.getString(R.string.erroMarcarConsultaPassado));
                                return;
                            }
                        }
                    }
                    PacienteController.getInstance().setHorarioConsulta(horario);
                    PacienteController.getInstance().setHoraConsultaInicial(horaInicial);
                    Intent intent = new Intent(tela, PacienteMarcaConsulta.class);
                    tela.startActivity(intent);
                }

            });
        }
    }

}
