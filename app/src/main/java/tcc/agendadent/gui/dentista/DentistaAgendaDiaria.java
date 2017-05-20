package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class DentistaAgendaDiaria extends LinearLayout implements Interface_Dentista {
    private Activity activity;
    private LinearLayout agendaDia;
    private static ViewFlipper flipper;
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    public static DateTime indiceSlider = DateTime.now();
    public static TextView header;
    public static int idLayout;
    public static ImageButton setaDireita;
    public static ImageButton setaEsquerda;
    private boolean firstTime;


    public DentistaAgendaDiaria(Activity activity, int id_janela) {
        super(activity);
        firstTime = true;
        this.activity = activity;
        this.id = id_janela;
        firstTime = true;
        idLayout = R.id.consultasDiarias;
        View.inflate(activity, R.layout.dentista_agenda_diaria_conteudo, this);
        indiceSlider = DateTime.now();
        //AgendaController.getInstance().limpaDados(activity);
        instanciaArtefatos();
        setEventos();
        setTextoData(DateTime.now());
        AgendaController.getInstance().setDiaAtual(DateTime.now().year().get(),DateTime.now().monthOfYear().get()
                    ,DateTime.now().dayOfMonth().get(),DateTime.now().hourOfDay().get());
        buscaAgendaDiaria();

    }

    private void blala() {
        //long idDentista, long idPaciente, long dataConsulta, long avaliacao, String tipoConsulta,String nomePaciente
        // day, hour, minute, second and milliseconds
        DateTime date0 = new DateTime(2017, 3, 29, 15, 00, 0, 0);
        DateTime date1 = new DateTime(2017, 3, 29, 15, 30, 0, 0);
        DateTime date2 = new DateTime(2017, 3, 29, 16, 00, 0, 0);
        DateTime date3 = new DateTime(2017, 3, 29, 16, 30, 0, 0);
//        DateTime date4 = new DateTime(2017, 3, 29, 11, 00, 0, 0);
//        DateTime date5 = new DateTime(2017, 3, 29, 11, 30, 0, 0);
//
//        Consulta c1 = new Consulta(1, 1, date0.getMillis(),0, "tipConsulta","nomePaciente1");
//        Consulta c2 = new Consulta(1, 1, date1.getMillis(),0, "tipConsulta","nomePaciente2");
//        Consulta c3 = new Consulta(1, 1, date2.getMillis(),0, "tipConsulta","nomePaciente2");
//        Consulta c4 = new Consulta(1, 1, date3.getMillis(),0, "tipConsulta","nomePaciente2");
//        Consulta c5 = new Consulta(1, 1, date4.getMillis(),0, "tipConsulta","nomePaciente3");
////        Consulta c6 = new Consulta(1, 1, date5.getMillis(),0, "tipConsulta","nomePaciente-1");
//        AgendaController.getInstance().insertConsulta(c1,"1","20171");
//        AgendaController.getInstance().insertConsulta(c2,"1","20171");
//        AgendaController.getInstance().insertConsulta(c3,"1","20171");
//        AgendaController.getInstance().insertConsulta(c4,"1","20171");
//        AgendaController.getInstance().insertConsulta(c5,"1","20171");
//        AgendaController.getInstance().insertConsulta(c6,"1","20171");
    }

    private void instanciaArtefatos() {
        agendaDia = (LinearLayout) findViewById(R.id.idAgendaDiariaLayout);
        slide_in_left = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(activity, R.anim.slide_out_right);
        flipper = (ViewFlipper) findViewById(R.id.idViewFlipper);
        header = (TextView) findViewById(R.id.textHeader);

        setaDireita = (ImageButton) findViewById(R.id.setaDireita);
        setaEsquerda = (ImageButton) findViewById(R.id.setaEsquerda);
    }

    private void setEventos() {

        agendaDia.setOnTouchListener(new OnSwipeTouchListener(activity) {

            public void onSwipeRight() {
                swipeRight();
            }

            public void onSwipeLeft() {
                swipeLeft();
            }
        });
        setaDireita.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               swipeLeft();
                                           }
                                       }
        );
        setaEsquerda.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                swipeRight();
                                            }
                                        }
        );
    }

    private void swipeLeft() {
        //cuidar desse
        if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias) //OK
            DentistaAgendaDiaria.idLayout = R.id.consultasDiarias3;
        else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias2)
            DentistaAgendaDiaria.idLayout = R.id.consultasDiarias;
        else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias3)
            DentistaAgendaDiaria.idLayout = R.id.consultasDiarias2;
        indiceSlider = indiceSlider.plusDays(1);
        flipper.setInAnimation(activity, R.anim.slide_in_right);
        flipper.setOutAnimation(activity, R.anim.slide_out_left);
        flipper.showPrevious();
        AgendaController.getInstance().slideDiaria(activity, AgendaController.getInstance().getConsultasSemestre(), indiceSlider);
    }

    private void swipeRight() {
        if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias)
            DentistaAgendaDiaria.idLayout = R.id.consultasDiarias2;
        else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias2)
            DentistaAgendaDiaria.idLayout = R.id.consultasDiarias3;
        else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias3)
            DentistaAgendaDiaria.idLayout = R.id.consultasDiarias;
        indiceSlider = indiceSlider.minusDays(1);
        flipper.setInAnimation(activity, R.anim.slide_in_left);
        flipper.setOutAnimation(activity, R.anim.slide_out_right);
        flipper.showNext();
        AgendaController.getInstance().slideDiaria(activity, AgendaController.getInstance().getConsultasSemestre(), indiceSlider);
    }


    private void buscaAgendaDiaria() {
        DialogAux.dialogCarregandoSimples(activity);
        int mes = DateTime.now().monthOfYear().get();
        String anoSemestre = DateTime.now().year().get() + "";
        if (mes >= 7) {
            anoSemestre = DateTime.now().year().get() + "A2";
        } else
            anoSemestre = DateTime.now().year().get() + "A1";
        anoSemestre = anoSemestre.replace("A", "");
        AgendaController.getInstance().getConsultasSemestre(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                , anoSemestre, activity, false);
    }


    @Override
    public void onResume() {
        if(activity==null){
            return;
        }
        if(firstTime){
            firstTime = false;
            return;
        }
        activity.recreate();
    }

    @Override
    public boolean needResume() {
        return true;
    }

    private int id;

    @Override
    public int getIdMenu() {
        return id;
    }

    public void flipper(boolean next) {
        flipperHelper(activity, next);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public static void flipperHelper(Activity tela, boolean next) {

        if (next) {
            if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias)
                DentistaAgendaDiaria.idLayout = R.id.consultasDiarias2;
            else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias2)
                DentistaAgendaDiaria.idLayout = R.id.consultasDiarias3;
            else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias3)
                DentistaAgendaDiaria.idLayout = R.id.consultasDiarias;
            indiceSlider = indiceSlider.minusDays(1);
            flipper.setInAnimation(tela, R.anim.slide_in_left);
            flipper.setOutAnimation(tela, R.anim.slide_out_right);
            flipper.showNext();
            AgendaController.getInstance().slideDiaria(tela, AgendaController.getInstance().getConsultasSemestre(), indiceSlider);
        } else {
            if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias) //OK
                DentistaAgendaDiaria.idLayout = R.id.consultasDiarias3;
            else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias2)
                DentistaAgendaDiaria.idLayout = R.id.consultasDiarias;
            else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias3)
                DentistaAgendaDiaria.idLayout = R.id.consultasDiarias2;
            indiceSlider = indiceSlider.plusDays(1);
            flipper.setInAnimation(tela, R.anim.slide_in_right);
            flipper.setOutAnimation(tela, R.anim.slide_out_left);
            flipper.showPrevious();
            AgendaController.getInstance().slideDiaria(tela, AgendaController.getInstance().getConsultasSemestre(), indiceSlider);

        }
    }

    public static void setTextoData(DateTime data) {
        String dia = "";
        String mes = "";
        if (data.getDayOfMonth()<10) {
            dia = "0" + data.getDayOfMonth();
        } else {
            dia = "" + data.getDayOfMonth();
        }

        if (data.getMonthOfYear()<10) {
            mes = "0" + data.getMonthOfYear();
        } else {
            mes = "" + data.getMonthOfYear();
        }
        AgendaController.getInstance().setDiaAtual(data.year().get(),data.monthOfYear().get()
                ,data.dayOfMonth().get(),data.hourOfDay().get());
        String dataString = dia + "/" + mes + "/" + data.getYear();
        header.setText(dataString);
//        header.setText("Agenda - " + dataString);
        AgendaController.getInstance().setMomento(data.getYear(), data.getMonthOfYear(),
                data.getDayOfMonth(), 11, 11);
        DialogAux.dialogCarregandoSimplesDismiss();
    }
}
