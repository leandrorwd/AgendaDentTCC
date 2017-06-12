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
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.EventosController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.paciente.PacienteMarcaConsulta;
import tcc.agendadent.objetos.AgendaSub;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
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

    public void insertConsulta(final Activity activity, final Consulta consulta, final String idDentista, final String semestreAno, final boolean direta) {
        try {
            consulta.setCancelada(false);
            firebaseDatabaseReference.child("agendaSub").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            consulta.setCancelada(false);
                            firebaseDatabaseReference
                                    .child("agendaSub")
                                    .child(idDentista)
                                    .child(semestreAno).child("consultasMarcadas").push()
                                    .setValue(consulta);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
            firebaseDatabaseReference.child("agendaSubPaciente").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            consulta.setCancelada(false);
                            firebaseDatabaseReference
                                    .child("agendaSubPaciente")
                                    .child(consulta.getIdPaciente() + "").push()
//                                    .child(semestreAno).child("consultasMarcadas").push()
                                    .setValue(consulta);
                            Intent intent = new Intent("kill");
                            intent.setType("text/plain");
                            if(direta){
                                activity.finish();
                                DentistaController.getInstance().callResume();
                                if(consulta.isTentarRemarcar()){
                                    //EventosController.getInstance().carregaListenersRemarcacao();
                                }
                            }

                            else{
                                activity.sendBroadcast(intent);

                            }
                            DialogAux.dialogCarregandoSimplesDismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
            String s = "treta";
        }
    }

    public void insertConsultaSecundaria(final Activity activity, final Consulta consulta, final String idDentista, final String semestreAno,Consulta consultaPrimaria) {
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

       //recortei new1
        } catch (Exception e) {
            String s = "treta";
        }
    }


    public void getConsultaSemestre(final long idDentista, final String anoSemestre, final Activity tela) {
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
                                if(!s1.getCancelada()){
                                    consultas.add(s1);
                                }

                            }
                            getConsultaSemestreNext(idDentista,anoSemestre,tela,consultas);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void getConsultaSemestreNext(final long id, final String anoSemestre, final Activity tela, final ArrayList<Consulta> consultas) {
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(id + "")
                    .child("20172")
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                Consulta s1 = new Consulta(consultaBanco);
                                s1.setIdConsulta(consultaBanco.getKey());
                                if(!s1.getCancelada()){
                                    consultas.add(s1);
                                }

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

    public void getConsultaSemestreCompleto(final long idDentista, final String anoSemestre, final Activity tela) {
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
                                if(!s1.getCancelada()){
                                    consultas.add(s1);
                                }
                            }
                            getConsultaSemestreCompletoNext(idDentista,anoSemestre,tela,consultas);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("batata");
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void getConsultaSemestreCompletoNext(final long idDentista, final String anoSemestre, final Activity tela, final ArrayList<Consulta> consultas) {
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(idDentista + "")
                    .child("20172")
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                Consulta s1 = new Consulta(consultaBanco);
                                s1.setIdConsulta(consultaBanco.getKey());
                                if(!s1.getCancelada()){
                                    consultas.add(s1);
                                }
                            }
                            AgendaController.getInstance().setAgendaSemestreAtual(consultas);

                            if (tela.getLocalClassName().equals("gui.paciente.PacienteVisualizaHorariosMarcacao")) {
                                AgendaController.getInstance().setAgendaMarcacao(tela, consultas);
                            }
                            else if (tela.getLocalClassName().equals("gui.dentista.DentistaAgendarConsultaEspecial")) {
                                AgendaController.getInstance().setAgendaCompleta(tela, consultas);
                            }else {
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
                                Consulta s1 = new Consulta(consultaBanco);
                                if(!s1.getCancelada()){
                                    if(!s1.isConsultaMultipla()){
                                        consultasBC.add(s1);
                                    }
                                }
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
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(consulta.getIdDentista() + "")
                    .child(semestreAno)
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                if (String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString())) {
                                    consultaBanco.getRef().child("cancelada").setValue(true);
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
                                    consultaBanco.getRef().child("cancelada").setValue(true);
                                }
                            }
                            DentistaController.getInstance().callResume();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }

        if(consulta.isConsultaMultiplaPai()) {

            try {
                firebaseDatabaseReference.child("agendaSub")
                        .child(consulta.getIdDentista() + "")
                        .child(semestreAno)
                        .child("consultasMarcadas")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                    try{
                                    if (String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsultaPrimaria").getValue().toString())) {
                                        consultaBanco.getRef().child("cancelada").setValue(true);
                                    }
                                    }catch (Exception e){
                                        continue;
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
                                    try{
                                    if ((String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsultaPrimaria").getValue().toString()))
                                            && (String.valueOf(consulta.getIdDentista()).equals(consultaBanco.child("idDentista").getValue().toString()))) {
                                        consultaBanco.getRef().child("cancelada").setValue(true);
                                    }}catch (Exception e) {continue;}
                                }
                                DentistaController.getInstance().callResume();
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            } catch (Exception e) {
            }

        }
        if(tela!=null){
            DialogAux.dialogOkSimplesFinish(tela, "Confirmação", "Consulta desmarcada com sucesso.");
        }
        DentistaController.getInstance().callResume();

    }

    public void reativaConsulta(final Activity tela, final Consulta consulta, String semestreAno) {
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(consulta.getIdDentista() + "")
                    .child(semestreAno)
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                if (String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString())) {
                                    consultaBanco.getRef().child("cancelada").setValue(true);
                                    DentistaController.getInstance().callResume();
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
                                    consultaBanco.getRef().child("cancelada").setValue(true);

                                }
                            }
                            DentistaController.getInstance().callResume();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }

        if(consulta.isConsultaMultiplaPai()) {

            try {
                firebaseDatabaseReference.child("agendaSub")
                        .child(consulta.getIdDentista() + "")
                        .child(semestreAno)
                        .child("consultasMarcadas")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                    try{
                                        if (String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsultaPrimaria").getValue().toString())) {
                                            consultaBanco.getRef().child("cancelada").setValue(true);
                                        }
                                    }catch (Exception e){
                                        continue;
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
                                    try{
                                        if ((String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsultaPrimaria").getValue().toString()))
                                                && (String.valueOf(consulta.getIdDentista()).equals(consultaBanco.child("idDentista").getValue().toString()))) {
                                            consultaBanco.getRef().child("cancelada").setValue(true);
                                        }}catch (Exception e) {continue;}
                                }
                                DentistaController.getInstance().callResume();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            } catch (Exception e) {
            }

        }
    }


    public void getPacienteViaMarcacaoConsulta(String email, final Activity activity, final int numeroHorarios) {
        final UsuarioPaciente[] p1 = new UsuarioPaciente[1];
        try{
            firebaseDatabaseReference.child("pacientes")
                    .orderByChild("email")
                    .equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                p1[0] = new UsuarioPaciente(child);
                            }
                            AgendaController.getInstance().navegaAgendaEspecial(activity,p1[0],numeroHorarios);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        catch (Exception e ){
        }
    }

    public void getConsultasListaEspera(final Activity activity) {
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
                            EventosController.getInstance().carregaListenersEspera(activity,indices);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
        }
    }

    public void checkConsultaLivre(final Consulta consulta, final String semestreAno) {
        try {
            firebaseDatabaseReference.child("agendaSub")
                    .child(consulta.getIdDentista() + "")
                    .child(semestreAno)
                    .child("consultasMarcadas")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean nenhumaMarcada = true;
                            for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                if (String.valueOf(consulta.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString())) {
                                    if(!Boolean.parseBoolean(String.valueOf(consultaBanco.child("cancelada").getValue()))){
                                        nenhumaMarcada = false;
                                    }
                                }
                            }
                            if(nenhumaMarcada){
                                PacienteMarcaConsulta.salvar();
                                //desmarcarConsulta(null,consulta,semestreAno);
                            }
                            else{
                                PacienteMarcaConsulta.dialogErro();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }
}
