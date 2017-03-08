package tcc.agendadent.objetos;

/**
 * Created by natha on 01/03/2017.
 */

public class Horario {
    private String horaInicial;
    private String horaFinal;
    private String duracao;
    private boolean[] diasSemana; //Domingo - Sabado

    public Horario(String horaInicial, String horaFinal, String duracao,boolean[] dias) {
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.duracao = duracao;
        this.diasSemana = dias;
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

    public boolean[] getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(boolean[] diasSemana) {
        this.diasSemana = diasSemana;
    }
}
