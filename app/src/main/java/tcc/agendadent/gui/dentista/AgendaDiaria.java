package tcc.agendadent.gui.dentista;

import android.app.Activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class AgendaDiaria extends LinearLayout implements ClassesDentista   {
    private Activity activity;
    private LinearLayout agendaDia;
    private static ViewFlipper flipper;
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;

    public AgendaDiaria(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_agenda_diaria_conteudo, this);
        buscaAgendaDiaria();
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        agendaDia = (LinearLayout) findViewById(R.id.idAgendaDiariaLayout);
        slide_in_left = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(activity, R.anim.slide_out_right);
        flipper = (ViewFlipper) findViewById(R.id.idViewFlipper);
    }

    private void setEventos() {

        agendaDia.setOnTouchListener(new OnSwipeTouchListener(activity) {

            public void onSwipeRight() {
                flipper.setInAnimation(activity, R.anim.slide_in_left);
                flipper.setOutAnimation(activity, R.anim.slide_out_right);
                flipper.showNext();

            }
            public void onSwipeLeft() {
                flipper.setInAnimation(activity, R.anim.slide_in_right);
                flipper.setOutAnimation(activity, R.anim.slide_out_left);
                flipper.showPrevious();
            }

        });
    }

    private void buscaAgendaDiaria() {
        DialogAux.dialogCarregandoSimples(activity);
        int mes = DateTime.now().monthOfYear().get();
        int ano = DateTime.now().year().get();
        String anoSemestre;
        if(mes>=7){
            anoSemestre = ano+2 +"";
        }
        else
            anoSemestre = ano+1 +"";
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
            flipper.setInAnimation(tela, R.anim.slide_in_left);
            flipper.setOutAnimation(tela, R.anim.slide_out_right);
            flipper.showNext();

        }
        else{
            flipper.setInAnimation(tela, R.anim.slide_in_right);
            flipper.setOutAnimation(tela, R.anim.slide_out_left);
            flipper.showPrevious();
        }
    }
}
