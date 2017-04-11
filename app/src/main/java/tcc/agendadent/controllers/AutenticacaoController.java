package tcc.agendadent.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.DentistaBC;
import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogCarregandoSimplesDismiss;
import static tcc.agendadent.servicos.DialogAux.dialogExcecoesFirebase;
import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;
import static tcc.agendadent.servicos.DialogAux.dialogOkSimplesFinish;

/**
 * Created by natha on 06/02/2017.
 */

public class AutenticacaoController {
    private static AutenticacaoController INSTANCE;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private AutenticacaoController() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static AutenticacaoController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutenticacaoController();
        }
        return INSTANCE;
    }

    public FirebaseAuth.AuthStateListener listenerLogin(FirebaseAuth.AuthStateListener mAuthListener) {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };
        return mAuthListener;
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void cadastraDentista(final Activity activity, final UsuarioDentista usuario, final String senha) {
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginCadastraDentista(activity, usuario, senha);
                        } else {
                            dialogCarregandoSimplesDismiss();
                            dialogExcecoesFirebase(activity, task.getException(), usuario.getEmail());
                        }
                    }
                });
    }

    private void loginCadastraDentista(final Activity tela, final UsuarioDentista usuario, String senha) {
        user = getCurrentUser();
        mAuth.signInWithEmailAndPassword(usuario.getEmail(), senha)
                .addOnCompleteListener(tela, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            dialogCarregandoSimplesDismiss();
                            dialogExcecoesFirebase(tela, task.getException(), usuario.getEmail());
                        } else {
                            CadastroController.getInstance().insertNewDentista(usuario, tela);
                        }
                    }
                });
    }

    public void cadastraPaciente(final Activity activity, final UsuarioPaciente usuario, final String senha) {
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginEnviaEmailCadastro(activity, usuario, senha);
                        } else {
                            dialogCarregandoSimplesDismiss();
                            dialogExcecoesFirebase(activity, task.getException(), usuario.getEmail());
                        }
                    }
                });
    }

    private void loginEnviaEmailCadastro(final Activity tela, final UsuarioPaciente usuario, String senha) {
        user = getCurrentUser();
        mAuth.signInWithEmailAndPassword(usuario.getEmail(), senha)
                .addOnCompleteListener(tela, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            dialogCarregandoSimplesDismiss();
                            dialogExcecoesFirebase(tela, task.getException(), usuario.getEmail());
                        } else {
                            enviaEmailCadastroConfirmacao(tela, usuario);
                        }
                    }
                });
    }

    private void enviaEmailCadastroConfirmacao(final Activity tela, final UsuarioPaciente usuario) {
        user = getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CadastroController.getInstance().insertNewPaciente(usuario);
                    dialogCarregandoSimplesDismiss();
                    dialogOkSimplesFinish(tela, tela.getString(R.string.Sucesso), tela.getString(R.string.emailConfirmacaoEnviado));
                } else {
                    dialogCarregandoSimplesDismiss();
                    dialogOkSimples(tela, tela.getString(R.string.erro), tela.getString(R.string.erroEmailConfirmacao));
                }
            }
        });
    }

    public void resetSenha(String email, final Activity activity) {
        String erro = activity.getResources().getString(R.string.erro);
        if (!ValidationTest.verificaInternet(activity)) {
            dialogOkSimples(activity, erro, activity.getResources().getString(R.string.internetSemConexao));
            return;
        }
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(activity.getLocalClassName().equals("gui.dentista.Main_Dentista")){
                            if (task.isSuccessful()) {
                                DialogAux.dialogCarregandoSimplesDismiss();
                                DialogAux.dialogOkSimples(activity, activity.getString(R.string.Sucesso), activity.getString(R.string.emailTroca));
                            } else {
                                DialogAux.dialogCarregandoSimplesDismiss();
                                DialogAux.dialogOkSimples(activity, activity.getString(R.string.erro), activity.getString(R.string.emailTrocaErro));
                            }
                        }
                        else{
                            if (task.isSuccessful()) {
                                DialogAux.dialogCarregandoSimplesDismiss();
                                DialogAux.dialogOkSimples(activity, activity.getString(R.string.Sucesso), activity.getString(R.string.emailRecuperacao));
                            } else {
                                DialogAux.dialogCarregandoSimplesDismiss();
                                DialogAux.dialogOkSimples(activity, activity.getString(R.string.erro), activity.getString(R.string.emailRecuperacaoErro));
                            }
                        }
                    }
                });
    }

    public void loginFirebase(final Activity tela, final String login, final String senha) {
        user = getCurrentUser();
        mAuth.signInWithEmailAndPassword(login, senha)
                .addOnCompleteListener(tela, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            DialogAux.dialogExcecoesFirebase(tela, task.getException(), login);
                        } else {
                            if (user.isEmailVerified()) {
                                DentistaBC dentista = new DentistaBC();
                                dentista.getDentistaViaEmailLogin(login, tela, true, senha);
                            } else {
                                emailNaoVerificado(tela, login, senha);
                            }

                        }
                    }
                });
    }

    public void loginFirebaseUsuario(final Activity tela, final String login, final String senha) {
        user = getCurrentUser();
        mAuth.signInWithEmailAndPassword(login, senha)
                .addOnCompleteListener(tela, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            DialogAux.dialogExcecoesFirebase(tela, task.getException(), login);
                        } else {
                            if (user.isEmailVerified()) {
                                PacienteBC paciente = new PacienteBC();
                                paciente.getPacienteViaEmailLogin(login, tela);
                            } else {
                                emailNaoVerificadoDialog(tela, login, false);
                            }

                        }
                    }
                });
    }

    public void emailNaoVerificado(final Activity tela, final String login, String senha) {
        DentistaBC dentista = new DentistaBC();
        dentista.getDentistaViaEmailLogin(login, tela, false, senha);
    }

    public void reenviaEmailConfirmacao(final Activity tela, String login) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DialogAux.dialogCarregandoSimplesDismiss();
                        if (task.isSuccessful()) {
                            dialogOkSimples(tela, tela.getString(R.string.Sucesso), tela.getString(R.string.emailConfirmacaoEnviado));
                        } else {
                            dialogOkSimples(tela, tela.getString(R.string.erro), tela.getString(R.string.erroEmailConfirmacao));
                        }
                    }
                });
    }

    public void emailNaoVerificadoDialog(final Activity tela, final String login, boolean dentista) {
        if (dentista) {
            dialogOkSimples(tela, tela.getResources().getString(R.string.erro), tela.getResources().getString(R.string.emailNaoConfirmadoMsgDentista));
            return;
        }
        new AlertDialog.Builder(tela)
                .setTitle(tela.getResources().getString(R.string.erro))
                .setMessage(tela.getResources().getString(R.string.emailNaoConfirmadoMsg))
                .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogAux.dialogCarregandoSimples(tela);
                        reenviaEmailConfirmacao(tela, login);
                    }
                })
                .setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    public boolean updatePerfilPaciente(UsuarioPaciente paciente) {
        PacienteBC pac = new PacienteBC();
        if (pac.updatePaciente(paciente)) {
            return true;
        }
        return false;
    }

}
