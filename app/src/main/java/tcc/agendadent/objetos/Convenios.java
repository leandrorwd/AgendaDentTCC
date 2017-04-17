package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by natha on 16/04/2017.
 */
public class Convenios {
    private boolean amilDental;
    private boolean belloDente;
    private boolean bradesco;
    private boolean doctorClin;
    private boolean interodonto;
    private boolean metlife;
    private boolean odontoEmpresa;
    private boolean sulAmerica;
    private boolean uniOdonto;

    public Convenios() {
    }

    public Convenios(boolean amilDental, boolean belloDente, boolean bradesco, boolean doctorClin, boolean interodonto, boolean metlife, boolean odontoEmpresa, boolean sulAmerica, boolean uniOdonto) {
        this.amilDental = amilDental;
        this.belloDente = belloDente;
        this.bradesco = bradesco;
        this.doctorClin = doctorClin;
        this.interodonto = interodonto;
        this.metlife = metlife;
        this.odontoEmpresa = odontoEmpresa;
        this.sulAmerica = sulAmerica;
        this.uniOdonto = uniOdonto;
    }

    public Convenios(DataSnapshot dataSnapshot) {
        this.amilDental = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("amilDental").getValue()));
        this.belloDente = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("belloDente").getValue()));
        this.bradesco = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("bradesco").getValue()));
        this.doctorClin = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("doctorClin").getValue()));
        this.interodonto = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("interodonto").getValue()));
        this.metlife = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("metlife").getValue()));
        this.odontoEmpresa = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("odontoEmpresa").getValue()));
        this.sulAmerica = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("sulAmerica").getValue()));
        this.uniOdonto = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("uniOdonto").getValue()));
    }


    public boolean isAmilDental() {
        return amilDental;
    }

    public void setAmilDental(boolean amilDental) {
        this.amilDental = amilDental;
    }

    public boolean isBelloDente() {
        return belloDente;
    }

    public void setBelloDente(boolean belloDente) {
        this.belloDente = belloDente;
    }

    public boolean isBradesco() {
        return bradesco;
    }

    public void setBradesco(boolean bradesco) {
        this.bradesco = bradesco;
    }

    public boolean isDoctorClin() {
        return doctorClin;
    }

    public void setDoctorClin(boolean doctorClin) {
        this.doctorClin = doctorClin;
    }

    public boolean isInterodonto() {
        return interodonto;
    }

    public void setInterodonto(boolean interodonto) {
        this.interodonto = interodonto;
    }

    public boolean isMetlife() {
        return metlife;
    }

    public void setMetlife(boolean metlife) {
        this.metlife = metlife;
    }

    public boolean isOdontoEmpresa() {
        return odontoEmpresa;
    }

    public void setOdontoEmpresa(boolean odontoEmpresa) {
        this.odontoEmpresa = odontoEmpresa;
    }

    public boolean isSulAmerica() {
        return sulAmerica;
    }

    public void setSulAmerica(boolean sulAmerica) {
        this.sulAmerica = sulAmerica;
    }

    public boolean isUniOdonto() {
        return uniOdonto;
    }

    public void setUniOdonto(boolean uniOdonto) {
        this.uniOdonto = uniOdonto;
    }
}
