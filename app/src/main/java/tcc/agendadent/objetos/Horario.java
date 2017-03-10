package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by natha on 01/03/2017.
 */

public class Horario {
    private String horaInicial;
    private String horaFinal;
    private String duracao;
   //Domingo - Sabado
    private List<Boolean> diasSemana;

    public Horario(String horaInicial, String horaFinal, String duracao,List<Boolean> dias) {
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.duracao = duracao;
        this.diasSemana = dias;
    }

    public Horario() {
    }

    public Horario(DataSnapshot dataSnapshot){
        this.horaInicial = String.valueOf(dataSnapshot.child("horaInicial").getValue());
        this.horaFinal = String.valueOf(dataSnapshot.child("horaFinal").getValue());
        this.duracao = String.valueOf(dataSnapshot.child("duracao").getValue());
        int i =0;
        diasSemana = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.child("diasSemana").getChildren()) {
            diasSemana.add( Boolean.parseBoolean(String.valueOf(child.getValue())));
            i++;
        }
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public List<Boolean> getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(List<Boolean> diasSemana) {
        this.diasSemana = diasSemana;
    }
}
