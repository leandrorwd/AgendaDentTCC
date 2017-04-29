package tcc.agendadent.bancoConnection;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.objetos.AgendaSub;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Work on 17/03/2017.
 */

public class AgendaBC {
    DatabaseReference firebaseDatabaseReference;

    public AgendaBC() {
        this.firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void insertAgendaSub(final AgendaSub agendaSub, final UsuarioDentista dentista) {
        try {
            firebaseDatabaseReference.child("agendaSub").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            firebaseDatabaseReference
                                    .child("agendaSub")
                                    .child(dentista.getIdDentista() + "")
                                    .child(agendaSub.getSemestreAno() + "")
                                    .setValue(agendaSub);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
            String s = "treta";
        }

    }

    public void insertConsulta(final Activity activity,final Consulta consulta, final String idDentista, final String semestreAno) {
        try {
            firebaseDatabaseReference.child("agendaSub").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            firebaseDatabaseReference
                                    .child("agendaSub")
                                    .child(idDentista)
                                    .child(semestreAno).child("consultasMarcadas").push()
                                    .setValue(consulta);
                            Intent intent = new Intent("kill");
                            intent.setType("text/plain");
                            activity.sendBroadcast(intent);
                            DialogAux.dialogCarregandoSimplesDismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
             firebaseDatabaseReference.child("agendaSubPaciente").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            firebaseDatabaseReference
                                    .child("agendaSubPaciente")
                                    .child(consulta.getIdPaciente()+"")
                                    .child(semestreAno).child("consultasMarcadas").push()
                                    .setValue(consulta);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
            String s = "treta";
        }
    }


    public void getConsultaSemestre(long idDentista, String anoSemestre, final Activity tela) {
        final ArrayList<Consulta> consultas = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(idDentista + "")
                    .child(anoSemestre)
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                Consulta s1 = new Consulta(consultaBanco);
                                s1.setIdConsulta(consultaBanco.getKey());
                                consultas.add(s1);
                            }
                            AgendaController.getInstance().setAgendaSemestreAtual(consultas);
                            AgendaController.getInstance().setAgendaDiaria(tela, consultas);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void getConsultaSemestreCompleto(long idDentista, String anoSemestre, final Activity tela) {
        final ArrayList<Consulta> consultas = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(idDentista + "")
                    .child(anoSemestre)
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                Consulta s1 = new Consulta(consultaBanco);
                                s1.setIdConsulta(consultaBanco.getKey());
                                consultas.add(s1);
                            }
                            AgendaController.getInstance().setAgendaSemestreAtual(consultas);

                            if(tela.getLocalClassName().equals("gui.paciente.PacienteVisualizaHorariosMarcacao")){
                                AgendaController.getInstance().setAgendaMarcacao(tela, consultas);
                            }
                            else{
                                AgendaController.getInstance().setAgendaCompleta(tela, consultas);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("batata");
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void getConsultasPaciente(final Activity tela, final int id) {
        final ArrayList<Consulta> consultasBC = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(String.valueOf(1))
                    .child("20171")
                    .child("consultasMarcadas")
                    .orderByChild("dataConsulta")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                consultasBC.add(new Consulta(consultaBanco));
                            }
                            AgendaController.getInstance().buscaAgendaBCAgendadas(tela, consultasBC, id);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
        }
    }

    public void getHistoricoConsultas (final Activity tela, final int id){
        final ArrayList<Consulta> consultasBC = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(String.valueOf(1))
                    .child("20171")
                    .child("consultasMarcadas")
                    .orderByChild("dataConsulta")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                Consulta s1 = new Consulta(consultaBanco);
                                s1.setIdConsulta(consultaBanco.getKey());
                                consultasBC.add(s1);
                            }
                            AgendaController.getInstance().buscaAgendaBCHistorico(tela, consultasBC, id);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
        }
    }


}
