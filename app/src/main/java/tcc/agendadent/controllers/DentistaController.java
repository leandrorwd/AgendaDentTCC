package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;

import tcc.agendadent.bancoConnection.DentistaBC;
import tcc.agendadent.gui.dentista.AgendaDiaria;
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

    private Activity telaAuxiliar;

    private DentistaController() {
        dentistaBC = new DentistaBC();
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
        activity.startActivity(new Intent(activity, AgendaDiaria.class));
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
                if((horarioNovo.getDiasSemana()[i]==h.getDiasSemana()[i]) && horarioNovo.getDiasSemana()[i])
                {
                    String[] parts =  horarioNovo.getHoraInicial().split(":");
                    int horaInicial = Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);

                    String[] parts2 =  horarioNovo.getHoraFinal().split(":");
                    int horaFinal = Integer.parseInt(parts2[0])*60+Integer.parseInt(parts2[1]);

                    String[] parts3 =  horarioNovo.getHoraInicial().split(":");
                    int hInicial = Integer.parseInt(parts3[0])*60+Integer.parseInt(parts3[1]);

                    String[] parts4 =  horarioNovo.getHoraFinal().split(":");
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
        return null;
    }
}
