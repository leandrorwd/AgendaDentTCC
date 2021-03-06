package tcc.agendadent.bancoConnection;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.layout_auxiliares.TemplateListaEspera;
import tcc.agendadent.gui.paciente.PacienteMarcaConsulta;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Endereco;
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

    public void getDentistasFiltro(final Activity activity, final String nomeDentista, final String tipoConsulta, final String planoSaude, final String especializacao, final Endereco endereco, final int distanciaKm, final double[] enderecoPaciente) {
        final ArrayList<UsuarioDentista> listaDentistas = new ArrayList<UsuarioDentista>();
        try{
            firebaseDatabaseReference.child("dentistas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                UsuarioDentista user = new UsuarioDentista(child);
                                listaDentistas.add(user);
                            }
                           PacienteController.getInstance().filtraDentistas(activity,nomeDentista,tipoConsulta,planoSaude,especializacao,endereco,distanciaKm,listaDentistas,enderecoPaciente);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        catch (Exception e ){
            System.out.println("TRETA");
        }
    }

    public void atualizaEndereco(Activity activity, Endereco e1) {
        try {
            firebaseDatabaseReference
                    .child("pacientes")
                    .child(String.valueOf(PacienteController.getInstance().getPacienteLogado().getIdPaciente()))
                    .child("endereco")
                    .setValue(e1);

                DialogAux.dialogCarregandoSimplesDismiss();
                PacienteController.getInstance().getPacienteLogado().setEndereco(e1);
                DialogAux.dialogOkSimplesInnerClassPacienteFinish(activity, activity.getString(R.string.Sucesso),
                        activity.getString(R.string.dadosSalvosSucesso));

        } catch (Exception e) {

        }

    }

    public void insertPaciente(final UsuarioPaciente usuarioPaciente, final String email, final Activity activity, final int selectedItemPosition) {
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
                            AgendaController.getInstance().setUsuarioPacienteConsultaEspecial(usuarioPaciente);
                            AgendaBC agendaBC = new AgendaBC();
                            agendaBC.getPacienteViaMarcacaoConsulta(email, activity, selectedItemPosition + 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void getDentistaViaId(final Activity activity, long idDentista, final PacienteMarcaConsulta pacienteMarcaConsulta) {
        final UsuarioDentista[] dentista = new UsuarioDentista[1];
        try {
            firebaseDatabaseReference.child("dentistas").child(idDentista+"")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                dentista[0] = new UsuarioDentista(dataSnapshot);

                            pacienteMarcaConsulta.instanciaArtefatosNotificacao(dentista[0]);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }
    public void getDentistaViaId(final Activity activity, long idDentista, final TemplateListaEspera listaEspera) {
        final UsuarioDentista[] dentista = new UsuarioDentista[1];
        try {
            firebaseDatabaseReference.child("dentistas").child(idDentista+"")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dentista[0] = new UsuarioDentista(dataSnapshot);

                            listaEspera.setDentista(dentista[0]);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void getListaEspera(final Activity activity) {
        final ArrayList<String> indices = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("pacientes")
                    .child(String.valueOf(PacienteController.getInstance().getPacienteLogado().getIdPaciente()))
                    .child("listaEspera")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                indices.add(String.valueOf(consultaBanco.getValue()));
                            }
                            getConsultasViaId(activity,indices);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
        }
    }

    private void getConsultasViaId(final Activity activity, final ArrayList<String> indices) {
         final ArrayList<Consulta> consultas = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("agendaSubPaciente")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                for (DataSnapshot todasConsultasPaciente : consultaBanco.getChildren()) {
                                    if(indices.contains(String.valueOf(todasConsultasPaciente.getKey()))){
                                        consultas.add(new Consulta(todasConsultasPaciente));
                                    }
                                }
                            }
                            PacienteController.getInstance().populaTelaListaEspera(activity,consultas);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
        }
    }
}
