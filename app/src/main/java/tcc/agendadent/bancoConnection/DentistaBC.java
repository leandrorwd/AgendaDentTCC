package tcc.agendadent.bancoConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AutenticacaoController;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by natha on 07/02/2017.
 */

public class DentistaBC {
    private com.google.firebase.storage.FirebaseStorage storage;
    DatabaseReference firebaseDatabaseReference;
    private StorageReference imageRef;
    private StorageReference storageRef;


    public DentistaBC() {
        storage = com.google.firebase.storage.FirebaseStorage.getInstance();
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef = storage.getReferenceFromUrl("gs://agenda-dent.appspot.com");
    }

    public void insertDentista(final UsuarioDentista dentista, final Activity tela) {
        try {
            firebaseDatabaseReference.child("dentistas").orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                dentista.setIdDentista(1);
                                firebaseDatabaseReference
                                        .child("dentistas")
                                        .child("1")
                                        .setValue(dentista);
                            } else {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    dentista.setIdDentista(Integer.parseInt(child.getKey()) + 1);
                                    firebaseDatabaseReference
                                            .child("dentistas")
                                            .child(String.valueOf(Integer.parseInt(child.getKey()) + 1))
                                            .setValue(dentista);
                                }
                            }

                            insertFotoPerfilDentista(dentista, tela);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void insertFotoPerfilDentista(final UsuarioDentista dentista, final Activity tela) {
        String save = "dentista/" + Math.random() + DateTime.now() + ".jpg";
        imageRef = storageRef.child(save);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CadastroController.getInstance().getFotoPerfilCadastro().compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                setFotoPerfilDentista(downloadUrl.toString(), dentista, tela);
            }
        });
    }

    private void setFotoPerfilDentista(String linkFoto, UsuarioDentista dentista, Activity tela) {
        try {
            firebaseDatabaseReference.child("dentistas")
                    .child(String.valueOf(dentista.getIdDentista()))
                    .child("urlFotoPerfil")
                    .setValue(linkFoto);
            if (tela.getLocalClassName().equals("gui.CadastroGui")) {
                FirebaseAuth.getInstance().signOut();
                DialogAux.dialogCarregandoSimplesDismiss();
                DialogAux.dialogOkSimplesFinish(tela, tela.getResources().getString(R.string.Aviso), tela.getResources().getString(R.string.cadastropendente));
            }
            if (tela.getLocalClassName().equals("gui.dentista.Main_Dentista")) {
                DialogAux.dialogCarregandoSimplesDismiss();
                DialogAux.dialogOkSimplesInnerClassDentistaFinish(tela, tela.getString(R.string.Sucesso),
                        tela.getString(R.string.dadosSalvosSucesso));
            }

        } catch (Exception e) {
        }
    }

    public void getDentistaViaEmailLogin(final String email, final Activity activity, final boolean verificado, final String senha) {
        final UsuarioDentista[] dentista = new UsuarioDentista[1];
        try {
            firebaseDatabaseReference.child("dentistas")
                    .orderByChild("email")
                    .equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                dentista[0] = new UsuarioDentista(child);
                            }
                            if (dentista[0] == null) {
                                AutenticacaoController.getInstance().loginFirebaseUsuario(activity, email, senha);
                                return;
                            }
                            if (dentista[0].isStatus()) {
                                DentistaController.getInstance().setUsuarioAtualLogin(dentista[0], activity);
                            } else {
                                AutenticacaoController.getInstance().emailNaoVerificadoDialog(activity, email, true);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void setDentista(Activity activity, UsuarioDentista usuario) {
        try {
            firebaseDatabaseReference
                    .child("dentistas")
                    .child(String.valueOf(usuario.getIdDentista()))
                    .setValue(usuario);
            DentistaController.getInstance().setDentista(activity, usuario, true);
        } catch (Exception e) {
            String s = "fufu";
        }
    }

    public void getHorarios(final Activity activity, final UsuarioDentista usuario) {
        final ArrayList<Horario> aux = new ArrayList<>();
        try {
            firebaseDatabaseReference
                    .child("dentistas")
                    .child(String.valueOf(usuario.getIdDentista()))
                    .child("agenda")
                    .child("horarios")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                aux.add(new Horario(child));
                            }
                            DentistaController.getInstance().getDentistaLogado().getAgenda().setHorarios(aux);
                            DentistaController.getInstance().carregaHorarios(activity, usuario, true);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
            String l = "lala";
        }
    }

    public void atualizaDentista(UsuarioDentista dentistaLogado, Activity activity, boolean upaFoto) {
        try {
            firebaseDatabaseReference
                    .child("dentistas")
                    .child(String.valueOf(dentistaLogado.getIdDentista()))
                    .setValue(dentistaLogado);
            if (upaFoto) {
                atualizaFotoPerfilDentista(dentistaLogado, activity);
            } else {
                DialogAux.dialogCarregandoSimplesDismiss();
                DialogAux.dialogOkSimplesInnerClassDentistaFinish(activity, activity.getString(R.string.Sucesso),
                        activity.getString(R.string.dadosSalvosSucesso));
            }

        } catch (Exception e) {

        }

    }

    private void atualizaFotoPerfilDentista(final UsuarioDentista dentista, final Activity tela) {
        String save = "dentista/" + Math.random() + DateTime.now() + ".jpg";
        imageRef = storageRef.child(save);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DentistaController.getInstance().getFotoPerfilCadastro().compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                setFotoPerfilDentista(downloadUrl.toString(), dentista, tela);
            }
        });
    }

    public void getTodosDentistas() {
        final ArrayList<UsuarioDentista> dentistasBC = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("dentistas")
                    .orderByChild("idDentista")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                dentistasBC.add(new UsuarioDentista(child));
                            }
                            DentistaController.getInstance().atualizaDentistas(dentistasBC);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void getTodosDentistasHistorico() {
        final ArrayList<UsuarioDentista> dentistasBC = new ArrayList<>();
        try {
            firebaseDatabaseReference.child("dentistas")
                    .orderByChild("idDentista")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                dentistasBC.add(new UsuarioDentista(child));
                            }
                            DentistaController.getInstance().atualizaDentistasHistorico(dentistasBC);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e) {
        }
    }
}