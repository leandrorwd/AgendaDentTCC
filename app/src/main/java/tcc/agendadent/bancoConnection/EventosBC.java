package tcc.agendadent.bancoConnection;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;

import tcc.agendadent.controllers.EventosController;
import tcc.agendadent.controllers.PacienteController;
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

    public void esperaConsulta(final Consulta consulta, final String idConsulta) {
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

        try {
            firebaseDatabaseReference.child("pacientes").child(PacienteController.getInstance().getPacienteLogado().getIdPaciente()+"")
                    .child("listaEspera").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                firebaseDatabaseReference
                                        .child("pacientes").child(PacienteController.getInstance().getPacienteLogado().getIdPaciente()+"")
                                        .child("listaEspera")
                                        .child("1")
                                        .setValue(idConsulta);
                            } else {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    firebaseDatabaseReference
                                            .child("pacientes").child(PacienteController.getInstance().getPacienteLogado().getIdPaciente()+"")
                                            .child("listaEspera")
                                            .child(String.valueOf(Integer.parseInt(child.getKey()) + 1))
                                            .setValue(idConsulta);
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

    public void esperaConsultaLogin(final Consulta consulta, final String idConsulta) {
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

    public void esperaConsultaLoginDentista(final Consulta consulta, final String idConsulta, String anoSemestre) {
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
                .child("agendaSub")
                .child(consulta.getIdDentista()+"")
                .child(anoSemestre)
                .child("consultasMarcadas")
                .child(idConsulta)
                .child("cancelada")
                .addValueEventListener(aux);

    }



    ValueEventListener aux;

    public void getConsultasListaEspera(final Activity activity, ArrayList<String> indices) {
        final ArrayList<String> canceladas = new ArrayList<>();
        final ArrayList<String> naoCanceladas = new ArrayList<>();
        final ArrayList<Consulta> consultaCanceladas = new ArrayList<>();
        final ArrayList<Consulta> consultaNaoCanceladas = new ArrayList<>();
        try {
            for (String indice : indices) {
                firebaseDatabaseReference.child("agendaSubPaciente")
                        .child(String.valueOf(PacienteController.getInstance().getPacienteLogado().getIdPaciente()))
                        .child(indice)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                boolean aux = Boolean.valueOf(String.valueOf(snapshot.child("cancelada").getValue()));
                                if (aux) {
                                    canceladas.add(snapshot.getKey());
                                    Consulta c1 = new Consulta(snapshot);
                                    if(c1.getDataFormat().isAfter(DateTime.now())){
                                        c1.setIdConsulta(snapshot.getKey());
                                        consultaCanceladas.add(c1);
                                        arrumaCanceladas(c1);
                                    }

                                } else {
                                    Consulta c1 = new Consulta(snapshot);
                                    if(c1.getDataFormat().isAfter(DateTime.now())){
                                        consultaNaoCanceladas.add(c1);
                                        naoCanceladas.add(snapshot.getKey());
                                        esperaConsultaLogin(c1, snapshot.getKey());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }

        } catch (Exception e) {
        }

    }

    private void arrumaCanceladas(final Consulta c) {

            DateTime date = c.getDataFormat();
            String datinha = date.getYear()+"";
            if(date.getMonthOfYear()>=7)
                 datinha = datinha+String.valueOf(2);
            else
                datinha = datinha+String.valueOf(1);

            final Consulta[] aux = {new Consulta()};
            try {
                final String finalDatinha = datinha;
                firebaseDatabaseReference.child("agendaSub")
                        .child(c.getIdDentista() + "")
                        .child(datinha)
                        .child("consultasMarcadas")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean existeOutraNoHorarioMarcada=false;
                                for (DataSnapshot consultaBanco : dataSnapshot.getChildren()) {
                                    if (String.valueOf(c.getDataConsulta()).equals(consultaBanco.child("dataConsulta").getValue().toString())) {
                                        if(!Boolean.valueOf(String.valueOf(consultaBanco.getRef().child("cancelada")))){
                                            existeOutraNoHorarioMarcada = true;
                                            aux[0] = new Consulta(consultaBanco);
                                            aux[0].setIdConsulta(consultaBanco.getKey());
                                        }
                                    }
                                }
                                if(existeOutraNoHorarioMarcada){
                                    esperaConsultaLoginDentista(aux[0],aux[0].getIdConsulta(), finalDatinha);
                                }
                                else{
                                    EventosController.getInstance().eventoConsulta(aux[0]);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            } catch (Exception e) {
            }
        }

    private ValueEventListener listener;
    public void setListenerConsulta(final Consulta consulta, final Activity activity) {
        final boolean[] firstTime = {true};
        listener=  new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    for (DataSnapshot consultinhas:snapshot.getChildren()) {
                        if(firstTime[0]){
                            firstTime[0] = false;
                            break;
                        }
                       //ESSE LISTENER CHAMA O METODO.
                        if(consulta.getDataFormat().isAfter(new DateTime(Long.parseLong(String.valueOf(consultinhas.child("dataConsulta").getValue()))))){
                                setListenerIndividual(consulta,activity,consultinhas.getKey());
                        }

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
        int mes = consulta.getDataFormat().monthOfYear().get();
        String anoSemestre;
        if(mes>=7){
            anoSemestre = consulta.getDataFormat().year().get() +"A2";
        }
        else
            anoSemestre = consulta.getDataFormat().year().get() +"A1";
        anoSemestre = anoSemestre.replace("A","");

        firebaseDatabaseReference
                .child("agendaSub")
                .child(consulta.getIdDentista()+"")
                .child(anoSemestre).child("consultasMarcadas")
                .addValueEventListener(listener);



    }

    private void setListenerIndividual(final Consulta consulta, final Activity activity, String key) {
        final boolean[] firstTime = {true};
        listener=  new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    if(firstTime[0]){
                        firstTime[0] = false;
                        return;
                    }
                    boolean aux = Boolean.valueOf(String.valueOf(snapshot.child("cancelada").getValue()));
                    if(aux){
                        EventosController.getInstance().eventoConsultaDesMarc(new Consulta((snapshot)));
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
        int mes = consulta.getDataFormat().monthOfYear().get();
        String anoSemestre;
        if(mes>=7){
            anoSemestre = consulta.getDataFormat().year().get() +"A2";
        }
        else
            anoSemestre = consulta.getDataFormat().year().get() +"A1";
        anoSemestre = anoSemestre.replace("A","");

        firebaseDatabaseReference
                .child("agendaSub")
                .child(consulta.getIdDentista()+"")
                .child(anoSemestre).child("consultasMarcadas").child(key)
                .addValueEventListener(listener);
    }
}

