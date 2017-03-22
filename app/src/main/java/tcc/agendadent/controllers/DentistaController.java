package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.DentistaBC;
import tcc.agendadent.gui.dentista.AgendaDiaria;
import tcc.agendadent.gui.dentista.ConfigAgenda;
import tcc.agendadent.gui.dentista.Main_Dentista;
import tcc.agendadent.gui.layout_auxiliares.Template_card_horario;
import tcc.agendadent.objetos.Agenda;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by natha on 09/02/2017.
 */

public class DentistaController {

    private static DentistaController INSTANCE;
    private DentistaBC dentistaBC;
    private UsuarioDentista dentistaLogado;
    private boolean horarioInicio;
    private boolean horarioTermino;
    private boolean horarioDuracao;
    private ArrayList<Horario> horariosTemporarios;
    private Activity telaAuxiliar;

    private DentistaController() {
        dentistaBC = new DentistaBC();
        horariosTemporarios = new ArrayList<>();
    }

    public static DentistaController getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DentistaController();
        }
        return INSTANCE;
    }

    public void setUsuarioAtualLogin(UsuarioDentista dentista, Activity activity) {
        dentistaLogado = dentista;
        DialogAux.dialogCarregandoSimplesDismiss();
        activity.startActivity(new Intent(activity, Main_Dentista.class));
    }

    public boolean isHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(boolean horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public boolean isHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(boolean horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public Activity getTelaAuxiliar() {
        return telaAuxiliar;
    }

    public void setTelaAuxiliar(Activity telaAuxiliar) {
        this.telaAuxiliar = telaAuxiliar;
    }

    public boolean isHorarioDuracao() {
        return horarioDuracao;
    }

    public void setHorarioDuracao(boolean horarioDuracao) {
        this.horarioDuracao = horarioDuracao;
    }

    public boolean verificaHorario(Horario horarioNovo) {
        for (Horario h : getAgenda().getHorarios()){
            int i = 0;
            while(i<7){
                if((horarioNovo.getDiasSemana().get(i)==h.getDiasSemana().get(i)) && horarioNovo.getDiasSemana().get(i))
                {
                    String[] parts =  horarioNovo.getHoraInicial().split(":");
                    int horaInicial = Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);

                    String[] parts2 =  horarioNovo.getHoraFinal().split(":");
                    int horaFinal = Integer.parseInt(parts2[0])*60+Integer.parseInt(parts2[1]);

                    String[] parts3 =  h.getHoraInicial().split(":");
                    int hInicial = Integer.parseInt(parts3[0])*60+Integer.parseInt(parts3[1]);

                    String[] parts4 =  h.getHoraFinal().split(":");
                    int hFInal = Integer.parseInt(parts4[0])*60+Integer.parseInt(parts4[1]);

                    if(horaInicial> hInicial && horaInicial< hFInal){
                        return false;
                    }
                    if(horaFinal> hInicial && horaFinal< hFInal){
                        return false;
                    }
                }
                i++;
            }
        }
        return true;
    }

    public Agenda getAgenda(){
        return dentistaLogado.getAgenda();
    }

    public void addHorarioAgenda(Horario horarioNovo,Activity tela) {
        horariosTemporarios.add(horarioNovo);
        getAgenda().addHorario(horarioNovo);
        tela.finish();
    }

    public ArrayList<Horario> getHorariosTemporarios() {
        return horariosTemporarios;
    }

    public void setDentista(Activity activity, UsuarioDentista usuario,boolean bancoChamada){
        if(bancoChamada){
            DialogAux.dialogCarregandoSimplesDismiss();
            activity.finish();
        }
        else{
            DialogAux.dialogCarregandoSimples(activity);
            dentistaBC.setDentista(activity,usuario);
        }
    }

    public UsuarioDentista getDentistaLogado() {
        return dentistaLogado;
    }

    public void setDentistaLogado(UsuarioDentista dentistaLogado) {
        this.dentistaLogado = dentistaLogado;
    }

    public void carregaHorarios(Activity activity, UsuarioDentista dentistaLogado, boolean banco) {
        if(banco){
            LinearLayout main =(LinearLayout)activity.findViewById(R.id.layoutPrincipalConfigAgenda);
            main.removeAllViews();
            for(Horario h : DentistaController.getInstance().getAgenda().getHorarios()){
                Template_card_horario t1 = new Template_card_horario(activity,activity,h);
                main.addView(t1);
            }
            DialogAux.dialogCarregandoSimplesDismiss();
        }
        else{
           DialogAux.dialogCarregandoSimples(activity);
           dentistaBC.getHorarios(activity,dentistaLogado);
        }
    }
}
