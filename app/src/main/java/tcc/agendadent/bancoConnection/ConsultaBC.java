package tcc.agendadent.bancoConnection;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Work on 17/03/2017.
 */

public class ConsultaBC {
    DatabaseReference firebaseDatabaseReference;

    public ConsultaBC() {
        this.firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
