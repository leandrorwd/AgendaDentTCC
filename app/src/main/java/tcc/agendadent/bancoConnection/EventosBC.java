package tcc.agendadent.bancoConnection;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tcc.agendadent.controllers.EventosController;
import tcc.agendadent.objetos.Consulta;

/**
 * Created by Work on 22/05/2017.
 */

public class EventosBC {
    static DatabaseReference firebaseDatabaseReference;
    public EventosBC() {
        this.firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void getKeyConsulta(final Consulta consulta){
        try {
            firebaseDatabaseReference.child("agendaSubPaciente")
                    .child(consulta.getIdPaciente() + "")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                if ((String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString()))
                                        && (String.valueOf(consulta.getIdDentista()).equals(consultaBanco.child("idDentista").getValue().toString()))) {
                                    if(!Boolean.valueOf(String.valueOf(consultaBanco.child("cancelada").getValue()))){
                                        esperaConsulta(consulta,consultaBanco.getRef().getKey().toString());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }
    public void esperaConsulta(final Consulta consulta,String idConsulta) {
        aux=  new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    boolean aux = Boolean.valueOf(String.valueOf(snapshot.getValue()));
                    if(aux){
                        EventosController.getInstance().eventoConsulta(consulta);
                    }

                }
                catch(Exception e){
                    Log.v("Teste","Teste");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseDatabaseReference
                .child("agendaSubPaciente")
                .child(consulta.getIdPaciente()+"")
                .child(idConsulta).child("cancelada")
                .addValueEventListener(aux);
    }

    ValueEventListener aux;
}
