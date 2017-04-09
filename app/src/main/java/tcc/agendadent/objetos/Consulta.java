package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 * Created by natha on 16/03/2017.
 */

public class Consulta implements Serializable {
    private long idDentista;
    private long idPaciente;
    private String nomePaciente;
    private long dataConsulta;
    private long avaliacao;
    private String tipoConsulta;

    public Consulta() {
    }

    public Consulta(long idDentista, long idPaciente, long dataConsulta, long avaliacao, String tipoConsulta,String nomePaciente) {
        this.idDentista = idDentista;
        this.idPaciente = idPaciente;
        this.dataConsulta = dataConsulta;
        this.avaliacao = avaliacao;
        this.tipoConsulta = tipoConsulta;
        this.nomePaciente = nomePaciente;
    }


    public Consulta(DataSnapshot dataSnapshot){
        this.idDentista = Long.parseLong(String.valueOf(dataSnapshot.child("idDentista").getValue()));
        this.idPaciente = Long.parseLong(String.valueOf(dataSnapshot.child("idPaciente").getValue()));
        this.dataConsulta = Long.parseLong(String.valueOf(dataSnapshot.child("dataConsulta").getValue()));
        this.avaliacao = Long.parseLong(String.valueOf(dataSnapshot.child("avaliacao").getValue()));
        this.tipoConsulta = String.valueOf(dataSnapshot.child("tipoConsulta").getValue());
        this.nomePaciente = String.valueOf(dataSnapshot.child("nomePaciente").getValue());
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

    public DateTime getDataFormat(){
        return  new DateTime(dataConsulta);
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }
}
