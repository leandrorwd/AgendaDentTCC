package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by natha on 01/03/2017.
 */

public class Horario implements Comparable<Horario>{
    private String horaInicial;
    private String horaFinal;
    private String duracao;
   //Domingo - Sabado
    private List<Boolean> diasSemana;
    private boolean particular;
    private boolean convenio;
    private boolean sus;

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
        this.particular = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("particular").getValue()));
        this.sus = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("sus").getValue()));
        this.convenio = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("convenio").getValue()));
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


    @Override
    public int compareTo(Horario o) {

        String[] parts =  this.getHoraInicial().split(":");
        int horaInicial1 = Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);

        String[] parts3 =  o.getHoraInicial().split(":");
        int horaInicial2 = Integer.parseInt(parts3[0])*60+Integer.parseInt(parts3[1]);

        if(horaInicial1<horaInicial2) return -1;
        else return 1;
    }

    public boolean isParticular() {
        return particular;
    }

    public void setParticular(boolean particular) {
        this.particular = particular;
    }

    public boolean isConvenio() {
        return convenio;
    }

    public void setConvenio(boolean convenio) {
        this.convenio = convenio;
    }

    public boolean isSus() {
        return sus;
    }

    public void setSus(boolean sus) {
        this.sus = sus;
    }
}
