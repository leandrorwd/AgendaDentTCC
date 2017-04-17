package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by natha on 16/04/2017.
 */

public class Especializacoes {
    private boolean clinicoGeral;
    private boolean endodontia;
    private boolean ortodontia;
    private boolean odontopediatria;
    private boolean odontologiaEstetica;
    private boolean protese;
    private boolean implantodontia;

    public Especializacoes() {
    }


    public Especializacoes(DataSnapshot dataSnapshot) {
        this.clinicoGeral =  Boolean.parseBoolean(String.valueOf(dataSnapshot.child("clinicoGeral").getValue()));
        this.endodontia = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("endodontia").getValue()));
        this.ortodontia = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("ortodontia").getValue()));
        this.odontopediatria = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("odontopediatria").getValue()));
        this.odontologiaEstetica = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("odontologiaEstetica").getValue()));
        this.protese = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("protese").getValue()));
        this.implantodontia = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("implantodontia").getValue()));

    }

    public Especializacoes(boolean clinicoGeral, boolean endodontia, boolean ortodontia, boolean odontopediatria, boolean odontologiaEstetica, boolean protese, boolean implantodontia) {
        this.clinicoGeral = clinicoGeral;
        this.endodontia = endodontia;
        this.ortodontia = ortodontia;
        this.odontopediatria = odontopediatria;
        this.odontologiaEstetica = odontologiaEstetica;
        this.protese = protese;
        this.implantodontia=implantodontia;
    }

    public boolean isImplantodontia() {
        return implantodontia;
    }

    public void setImplantodontia(boolean implantodontia) {
        this.implantodontia = implantodontia;
    }

    public boolean isClinicoGeral() {
        return clinicoGeral;
    }

    public void setClinicoGeral(boolean clinicoGeral) {
        this.clinicoGeral = clinicoGeral;
    }

    public boolean isEndodontia() {
        return endodontia;
    }

    public void setEndodontia(boolean endodontia) {
        this.endodontia = endodontia;
    }

    public boolean isOrtodontia() {
        return ortodontia;
    }

    public void setOrtodontia(boolean ortodontia) {
        this.ortodontia = ortodontia;
    }

    public boolean isOdontopediatria() {
        return odontopediatria;
    }

    public void setOdontopediatria(boolean odontopediatria) {
        this.odontopediatria = odontopediatria;
    }

    public boolean isOdontologiaEstetica() {
        return odontologiaEstetica;
    }

    public void setOdontologiaEstetica(boolean odontologiaEstetica) {
        this.odontologiaEstetica = odontologiaEstetica;
    }

    public boolean isProtese() {
        return protese;
    }

    public void setProtese(boolean protese) {
        this.protese = protese;
    }
}
