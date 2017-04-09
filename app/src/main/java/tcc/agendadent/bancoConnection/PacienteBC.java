package tcc.agendadent.bancoConnection;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by natha on 08/02/2017.
 */

public class PacienteBC {
    DatabaseReference firebaseDatabaseReference;

    public PacienteBC() {
        this.firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void insertPaciente(final UsuarioPaciente usuarioPaciente) {
        try {
            firebaseDatabaseReference.child("pacientes").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                usuarioPaciente.setIdPaciente(1);
                                firebaseDatabaseReference
                                        .child("pacientes")
                                        .child("1")
                                        .setValue(usuarioPaciente);
                            } else {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    usuarioPaciente.setIdPaciente(Integer.parseInt(child.getKey()) + 1);
                                    firebaseDatabaseReference
                                            .child("pacientes")
                                            .child(String.valueOf(Integer.parseInt(child.getKey()) + 1))
                                            .setValue(usuarioPaciente);
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

    public void getUsuarioTemp(Activity tela, String login) {

    }

    public void getPacienteViaEmailLogin(final String email, final Activity activity) {
        final UsuarioPaciente[] paciente = new UsuarioPaciente[1];
        try {
            firebaseDatabaseReference.child("pacientes")
                    .orderByChild("email")
                    .equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                paciente[0] = new UsuarioPaciente(child);
                            }

                            if (activity.getLocalClassName().equals("gui.LoginGui")) {// Nome da  a qual deseja testar para chamar o método de retorno do controller
                                PacienteController.getInstance().setUsuarioAtualLogin(paciente[0], activity);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }

    public boolean updatePaciente(final UsuarioPaciente paciente) {
        try {
            firebaseDatabaseReference.child("pacientes")
                    .orderByChild("email")
                    .equalTo(paciente.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            firebaseDatabaseReference
                                    .child("pacientes")
                                    .child(paciente.getIdPaciente()+"")
                                    .setValue(paciente);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void getPacienteConsultaDen(final Consulta consulta, final Activity tela) {
        try{
            firebaseDatabaseReference.child("pacientes")
                    .child(String.valueOf(consulta.getIdPaciente()))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UsuarioPaciente p1 = new UsuarioPaciente(dataSnapshot);
                    PacienteController.getInstance().getPacienteConsultaDen(consulta,true,p1,tela);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e ){
        }
    }
}
