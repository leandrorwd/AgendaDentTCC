package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by natha on 16/03/2017.
 */

public class Consulta implements Serializable {
    private long idDentista;
    private long idPaciente;
    private String nomePaciente;
    private long dataConsulta;
    private long dataConsultaPrimaria;

    private long duracao;
    private long avaliacao;
    private String tipoConsulta;
    private String idConsulta;
    private boolean cancelada;
    private boolean consultaMultipla;
    private boolean consultaMultiplaPai;
    private boolean tentarRemarcar;

    public Consulta() {
    }

    public Consulta(long idDentista, long idPaciente, long dataConsulta, long avaliacao,
                    String tipoConsulta, String nomePaciente, long duracao, boolean cancelada) {
        this.idDentista = idDentista;
        this.idPaciente = idPaciente;
        this.dataConsulta = dataConsulta;
        this.avaliacao = avaliacao;
        this.tipoConsulta = tipoConsulta;
        this.nomePaciente = nomePaciente;
        this.duracao = duracao;
        this.cancelada = cancelada;
        this.consultaMultipla = false;
    }

    public Consulta(DataSnapshot dataSnapshot) {
        this.idDentista = Long.parseLong(String.valueOf(dataSnapshot.child("idDentista").getValue()));
        this.idPaciente = Long.parseLong(String.valueOf(dataSnapshot.child("idPaciente").getValue()));
        this.dataConsulta = Long.parseLong(String.valueOf(dataSnapshot.child("dataConsulta").getValue()));
        try{
            this.dataConsultaPrimaria = Long.parseLong(String.valueOf(dataSnapshot.child("dataConsultaPrimaria").getValue()));
        }catch(Exception e){
            this.dataConsultaPrimaria = dataConsulta;
        }
        if(dataConsultaPrimaria==0){
            this.dataConsultaPrimaria = dataConsulta;
        }
        this.avaliacao = Long.parseLong(String.valueOf(dataSnapshot.child("avaliacao").getValue()));
        try {
            this.duracao = Long.parseLong(String.valueOf(dataSnapshot.child("duracao").getValue()));
        } catch (Exception e) {
            this.duracao = 1800000;
        }
        this.tipoConsulta = String.valueOf(dataSnapshot.child("tipoConsulta").getValue());
        this.nomePaciente = String.valueOf(dataSnapshot.child("nomePaciente").getValue());
        this.cancelada = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("cancelada").getValue()));
        try{
        this.consultaMultipla = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("consultaMultipla").getValue()));}
        catch (Exception e){
            consultaMultipla = false;
        }
        try{
            this.consultaMultiplaPai = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("consultaMultiplaPai").getValue()));}
        catch (Exception e){
            consultaMultipla = false;
        }
        try{
            this.tentarRemarcar = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("tentarRemarcar").getValue()));}
        catch (Exception e){
            tentarRemarcar = false;
        }


    }

    public long getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(long idDentista) {
        this.idDentista = idDentista;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public long getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(long dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public long getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(long avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public DateTime getDataFormat() {
        return new DateTime(dataConsulta);
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public long getDuracao() {
        return duracao;
    }

    public DateTime getHoraFinalDateTime() {
        return new DateTime(dataConsulta + duracao);
    }

    public void setIdConsulta(String idConsulta) { this.idConsulta = idConsulta; }

    public String getIdConsulta(){return idConsulta;}

    public boolean getCancelada() { return cancelada; }

    public void setCancelada(boolean cancelada) { this.cancelada = cancelada; }

    public boolean isConsultaMultipla() {
        return consultaMultipla;
    }

    public void setConsultaMultipla(boolean consultaMultipla) {
        this.consultaMultipla = consultaMultipla;
    }

    public void setDuracao(long duracao) {
        this.duracao = duracao;
    }

    public long getDataConsultaPrimaria() {
        return dataConsultaPrimaria;
    }

    public void setDataConsultaPrimaria(long dataConsultaPrimaria) {
        this.dataConsultaPrimaria = dataConsultaPrimaria;
    }


    public boolean isConsultaMultiplaPai() {
        return consultaMultiplaPai;
    }



    public void setConsultaMultiplaPai(boolean consultaMultiplaPai) {
        this.consultaMultiplaPai = consultaMultiplaPai;
    }

    public boolean isTentarRemarcar() {
        return tentarRemarcar;
    }

    public void setTentarRemarcar(boolean tentarRemarcar) {
        this.tentarRemarcar = tentarRemarcar;
    }
}
