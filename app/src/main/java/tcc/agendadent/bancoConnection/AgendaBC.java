package tcc.agendadent.bancoConnection;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

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
    static DatabaseReference firebaseDatabaseReference;
    public int count;

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

    public void insertConsulta(final Activity activity, final Consulta consulta, final String idDentista, final String semestreAno) {
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
                                    .child(consulta.getIdPaciente() + "").push()
//                                    .child(semestreAno).child("consultasMarcadas").push()
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

                            if (tela.getLocalClassName().equals("gui.paciente.PacienteVisualizaHorariosMarcacao")) {
                                AgendaController.getInstance().setAgendaMarcacao(tela, consultas);
                            } else {
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

    public void getConsultasPaciente(long idPaciente, final Activity tela, final LinearLayout layout, final boolean consulta) {
        final ArrayList<Consulta> consultasBC = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("agendaSubPaciente")
                    .child(String.valueOf(idPaciente + ""))
                    .orderByChild("dataConsulta")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                consultasBC.add(new Consulta(consultaBanco));
                            }
                            AgendaController.getInstance().buscaAgendaBCAgendadas(tela, consultasBC, layout, consulta);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
        }
    }

    public void desmarcarConsulta(final Activity tela, final Consulta consulta, String semestreAno) {
//        AgendaController.getInstance().setCount(0);
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(consulta.getIdDentista() + "")
                    .child("20171")
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                if (String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString())) {
                                    consultaBanco.getRef().removeValue();
//                                    AgendaController.getInstance().incrCount();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }

        try {
            firebaseDatabaseReference.child("agendaSubPaciente")
                    .child(consulta.getIdPaciente() + "")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                if ((String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString()))
                                        && (String.valueOf(consulta.getIdDentista()).equals(consultaBanco.child("idDentista").getValue().toString()))) {
                                    consultaBanco.getRef().removeValue();
//                                    AgendaController.getInstance().incrCount();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
//        DialogAux.dialogCarregandoSimplesDismiss();
//        if (AgendaController.getInstance().getCount() == 0) {
//            DialogAux.dialogOkSimplesFinish(tela, "Confirmação", "A consulta não pode ser removida.");
//        } else if (AgendaController.getInstance().getCount() == 1) {
//            DialogAux.dialogOkSimplesFinish(tela, "Confirmação", "Ocorreu um problema com uma das exclusões.");
//        } else if (AgendaController.getInstance().getCount() == 2) {
            DialogAux.dialogOkSimplesFinish(tela, "Confirmação", "Consulta removida com sucesso.");
//        }
    }

//    public void incrCount() {
//        count++;
//    }
//
//    public void setCount(int i) {
//        count = i;
//    }
//
//    public int getCount(){
//        return count;
//    }
}
