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
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.dentista.DentistaAgendaDiaria;
import tcc.agendadent.gui.dentista.DentistaVisualizarConsulta;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class TemplateConsultaAgenda extends RelativeLayout {

    private Activity tela;
    private Consulta consulta;
    private String usuarioTipo;
    private String horaInicial;
    public TemplateConsultaAgenda(Activity tela, Consulta c1,String usuario) {
        super(tela);
        this.tela=tela;
        usuarioTipo=usuario;
        View.inflate(tela, R.layout.template_consulta_agenda, this);
        setEventos();
        preencheHorario(c1);
        consulta = c1;
        setEventos();
    }
    public TemplateConsultaAgenda(Activity tela, String horaInicial,String usuario) {
        super(tela);
        this.tela=tela;
        usuarioTipo=usuario;
        View.inflate(tela, R.layout.template_consulta_agenda, this);
        setEventos();
        this.horaInicial = horaInicial;
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

    private void preencheHorario(Consulta c) {
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
                    DentistaAgendaDiaria.flipperHelper(tela,true);
                }

                public void onSwipeLeft() {
                    DentistaAgendaDiaria.flipperHelper(tela,false);
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

                    Intent i = new Intent(tela, DentistaVisualizarConsulta.class);
                    if(usuarioTipo.equals("dentista")){
                        if(consulta!=null){
                            i.putExtra("consulta", consulta);
                            i.putExtra("user",usuarioTipo);
                            tela.startActivity(i);
                        }
                        else{
                            dialogSuspender();
                        }
                    }
                   else{
                        i.putExtra("consulta", consulta);
                        i.putExtra("user",usuarioTipo);
                        tela.startActivity(i);
                   }

                }

            });
        }
    }

    private void dialogSuspender() {
        new AlertDialog.Builder(tela)
                .setTitle(tela.getResources().getString(R.string.NaoHaConsultaMarcada))
                .setMessage(tela.getResources().getString(R.string.ocuparConsulta))
                .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DateTime aux = DateTime.now();
                        int hora =Integer.parseInt( horaInicial.substring(0,2));
                        int min = Integer.parseInt( horaInicial.substring(3,5));
                        try{
                            String operador = aux.toString().substring(23,26);
                            int valor = Integer.parseInt(aux.toString().substring(24,26));
                            if(operador.contains("+")){
                                 hora = hora+valor;
                            }
                            else{
                              hora = hora -valor;
                            }
                        }catch (Exception e){

                        }
                        DateTime date0 = new DateTime(AgendaController.getInstance().getMomento().getYear(),
                                AgendaController.getInstance().getMomento().getMonthOfYear(),
                                AgendaController.getInstance().getMomento().getDayOfMonth(),hora,min);
                        Consulta c1 = new Consulta(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                                , 0, date0.getMillis(),0, "-","Indisponivel");
                        String anoSemestre;
                        if(date0.getMonthOfYear()>=7){
                            anoSemestre = DateTime.now().year().get() +"A2";
                        }
                        else
                            anoSemestre = DateTime.now().year().get() +"A1";
                        anoSemestre = anoSemestre.replace("A","");
                        AgendaController.getInstance().insertConsulta(c1,DentistaController.getInstance().getDentistaLogado().getIdDentista()+""
                                ,anoSemestre);


                    }
                })
                .setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .show();
    }
}
