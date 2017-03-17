package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

/**
 * Created by natha on 16/03/2017.
 */

public class AgendaSub {
    private long idDentista;
    private long semestreAno;
    private ArrayList<Consulta> consultasMarcadas;

    public AgendaSub() {

    }

    public AgendaSub(long idDentista, long semestreAno, ArrayList<Consulta> consultasMarcadas) {
        this.idDentista = idDentista;
        this.semestreAno = semestreAno;
        this.consultasMarcadas = consultasMarcadas;
    }

    public AgendaSub(DataSnapshot dataSnapshot){
        this.idDentista = Long.parseLong(String.valueOf(dataSnapshot.child("semestreAno").getValue()));
        this.semestreAno = Long.parseLong(String.valueOf(dataSnapshot.child("semestreAno").getValue()));
        GenericTypeIndicator< ArrayList<Consulta>> t = new GenericTypeIndicator< ArrayList<Consulta>>() {};
        consultasMarcadas =dataSnapshot.child("consultasMarcadas").getValue(t);
    }

}
