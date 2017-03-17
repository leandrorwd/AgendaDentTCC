package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by natha on 01/03/2017.
 */

public class Agenda {
    private ArrayList<Horario> horarios;
    private ArrayList<Long> semestreAno;

    public Agenda() {
        this.horarios = new ArrayList<>();
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }

    public void addHorario(Horario h){
        horarios.add(h);
    }

    public Agenda(DataSnapshot dataSnapshot){
        ArrayList<Horario> horarios = new ArrayList<Horario>();
        for (DataSnapshot child : dataSnapshot.child("agenda").getChildren()) {
            horarios.add(dataSnapshot.child("agenda").getValue(Horario.class));
        }
    }

    public ArrayList<Long> getSemestreAno() {
        return semestreAno;
    }

    public void setSemestreAno(ArrayList<Long> semestreAno) {
        this.semestreAno = semestreAno;
    }
}
