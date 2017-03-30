package tcc.agendadent.gui.dentista;

import android.app.Activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class AgendaDiaria extends LinearLayout implements ClassesDentista   {
    private Activity activity;
    private LinearLayout agendaDia;
    private static ViewFlipper flipper;
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    private static DateTime indiceSlider = DateTime.now();
    private static TextView header;
    public  static int idLayout;
    public AgendaDiaria(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        idLayout = R.id.consultasDiarias;
        View.inflate(activity, R.layout.activity_agenda_diaria_conteudo, this);
        buscaAgendaDiaria();
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
        setTextoData(DateTime.now());

        // blala();
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

        Consulta c1 = new Consulta(1, 1, date0.getMillis(),0, "tipConsulta","nomePaciente1");
        Consulta c2 = new Consulta(1, 1, date1.getMillis(),0, "tipConsulta","nomePaciente2");
        Consulta c3 = new Consulta(1, 1, date2.getMillis(),0, "tipConsulta","nomePaciente2");
        Consulta c4 = new Consulta(1, 1, date3.getMillis(),0, "tipConsulta","nomePaciente2");
//        Consulta c5 = new Consulta(1, 1, date4.getMillis(),0, "tipConsulta","nomePaciente3");
//        Consulta c6 = new Consulta(1, 1, date5.getMillis(),0, "tipConsulta","nomePaciente-1");
        AgendaController.getInstance().insertConsulta(c1,"1","20171");
        AgendaController.getInstance().insertConsulta(c2,"1","20171");
        AgendaController.getInstance().insertConsulta(c3,"1","20171");
        AgendaController.getInstance().insertConsulta(c4,"1","20171");
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

    }

    private void setEventos() {

        agendaDia.setOnTouchListener(new OnSwipeTouchListener(activity) {

            public void onSwipeRight() {
                if(AgendaDiaria.idLayout ==R.id.consultasDiarias)
                    AgendaDiaria.idLayout = R.id.consultasDiarias2;
                else if(AgendaDiaria.idLayout ==R.id.consultasDiarias2)
                    AgendaDiaria.idLayout = R.id.consultasDiarias3;
                else if(AgendaDiaria.idLayout ==R.id.consultasDiarias3)
                    AgendaDiaria.idLayout = R.id.consultasDiarias;
                indiceSlider = indiceSlider.minusDays(1);
                flipper.setInAnimation(activity, R.anim.slide_in_left);
                flipper.setOutAnimation(activity, R.anim.slide_out_right);
                flipper.showNext();
                AgendaController.getInstance().slideDiaria(activity,AgendaController.getInstance().getConsultasSemestre(),indiceSlider);
            }
            public void onSwipeLeft() {
                //cuidar desse
                if(AgendaDiaria.idLayout ==R.id.consultasDiarias) //OK
                    AgendaDiaria.idLayout = R.id.consultasDiarias3;
                else if(AgendaDiaria.idLayout ==R.id.consultasDiarias2)
                    AgendaDiaria.idLayout = R.id.consultasDiarias;
                else if(AgendaDiaria.idLayout ==R.id.consultasDiarias3)
                    AgendaDiaria.idLayout = R.id.consultasDiarias2;
                indiceSlider = indiceSlider.plusDays(1);
                flipper.setInAnimation(activity, R.anim.slide_in_right);
                flipper.setOutAnimation(activity, R.anim.slide_out_left);
                flipper.showPrevious();
                AgendaController.getInstance().slideDiaria(activity,AgendaController.getInstance().getConsultasSemestre(),indiceSlider);
            }

        });
    }

    private void buscaAgendaDiaria() {
        DialogAux.dialogCarregandoSimples(activity);
        int mes = DateTime.now().monthOfYear().get();
        String anoSemestre= DateTime.now().year().get()+"";
        if(mes>=7){
            anoSemestre = DateTime.now().year().get() +"A2";
        }
        else
            anoSemestre = DateTime.now().year().get() +"A1";
        anoSemestre = anoSemestre.replace("A","");
        AgendaController.getInstance().getConsultasSemestre(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                ,anoSemestre,activity,false);
    }


    @Override
    public void onResume() {
        //buscaAgendaDiaria();
    }

    @Override
    public boolean needResume() {
        return false;
    }
    private int id;
    @Override
    public int getIdMenu(){
        return id;
    }

    public void flipper(boolean next){
        flipperHelper(activity, next);
    }
    public static void flipperHelper(Activity tela, boolean next){

        if(next){
            if(AgendaDiaria.idLayout ==R.id.consultasDiarias)
                AgendaDiaria.idLayout = R.id.consultasDiarias2;
            else if(AgendaDiaria.idLayout ==R.id.consultasDiarias2)
                AgendaDiaria.idLayout = R.id.consultasDiarias3;
            else if(AgendaDiaria.idLayout ==R.id.consultasDiarias3)
                AgendaDiaria.idLayout = R.id.consultasDiarias;
            indiceSlider = indiceSlider.minusDays(1);
            flipper.setInAnimation(tela, R.anim.slide_in_left);
            flipper.setOutAnimation(tela, R.anim.slide_out_right);
            flipper.showNext();
            AgendaController.getInstance().slideDiaria(tela,AgendaController.getInstance().getConsultasSemestre(),indiceSlider);
        }
        else{
            if(AgendaDiaria.idLayout ==R.id.consultasDiarias) //OK
                AgendaDiaria.idLayout = R.id.consultasDiarias3;
            else if(AgendaDiaria.idLayout ==R.id.consultasDiarias2)
                AgendaDiaria.idLayout = R.id.consultasDiarias;
            else if(AgendaDiaria.idLayout ==R.id.consultasDiarias3)
                AgendaDiaria.idLayout = R.id.consultasDiarias2;
            indiceSlider = indiceSlider.plusDays(1);
            flipper.setInAnimation(tela, R.anim.slide_in_right);
            flipper.setOutAnimation(tela, R.anim.slide_out_left);
            flipper.showPrevious();
            AgendaController.getInstance().slideDiaria(tela,AgendaController.getInstance().getConsultasSemestre(),indiceSlider);

        }



    }

    public static void setTextoData(DateTime data){
        String dataString = data.getDayOfMonth() + "/"+ data.getMonthOfYear()+"/"+data.getYear();
        header.setText("Agenda - "+ dataString);

    }
}
