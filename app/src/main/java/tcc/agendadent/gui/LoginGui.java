package tcc.agendadent.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AutentificacaoController;
import tcc.agendadent.controllers.CadastroController;
import tcc.agendadent.servicos.DialogAux;

import static tcc.agendadent.servicos.DialogAux.dialogCarregandoSimples;

public class LoginGui extends AppCompatActivity {

    public static int telaCadastro;
    private Button botaoCadastro;
    private Button botaoLogin;
    private Button botaoRecuperar;
    private Button botaoDentista; //temporario
    private Button botaoPaciente; //temporario
    private EditText emailLogin;
    private EditText senhaLogin;
    private CheckBox lembraEmail;
    private TextInputLayout emailLoginExterno;
    private TextInputLayout senhaLoginExterno;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PackageManager manager;

    ///Teste
    //Leandro
    //Leandro2
    //Nathan
    //Teste PC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = LoginGui.this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(LoginGui.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView version  = (TextView) findViewById(R.id.idVersion);
        version.setText(info.versionName);

        instanciaArtefatos();
        setEventos();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void setEventos() {
        mAuthListener = AutentificacaoController.getInstance().listenerLogin(mAuthListener);
        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginGui.this, CadastroGui.class);
                startActivity(intent);
            }
        });

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoLogin();
            }
        });
        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoRecuperarSenha();
            }
        });

        //temporario:

        botaoPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoPaciente();
            }
        });
        botaoDentista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoDentista();
            }
        });

    }


    private void botaoLogin() {
        dialogCarregandoSimples(LoginGui.this);
        if (lembraEmail.isChecked()) {
            editor.putString("email", emailLogin.getText().toString());
            editor.commit();
        } else {
            editor.putString("email", null);
            editor.commit();
        }
        AutentificacaoController.getInstance().loginFirebase(LoginGui.this, emailLogin.getText().toString(), senhaLogin.getText().toString());
    }

    //temporario

    private void botaoDentista() {
        dialogCarregandoSimples(LoginGui.this);
        AutentificacaoController.getInstance().loginFirebase(LoginGui.this, "nathan.dalben.flores@gmail.com", "123456");
    }

    //temporario

    private void botaoPaciente() {
        dialogCarregandoSimples(LoginGui.this);
        AutentificacaoController.getInstance().loginFirebase(LoginGui.this, "leandro.rwd@gmail.com", "123456");
    }


    private void botaoRecuperarSenha() {
        new AlertDialog.Builder(LoginGui.this)
                .setTitle(LoginGui.this.getResources().getString(R.string.recuperarSenha))
                .setMessage(LoginGui.this.getResources().getString(R.string.trocarSenhaDialog) + emailLogin.getText().toString() + " ?")
                .setPositiveButton(LoginGui.this.getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarregandoSimples(LoginGui.this);
                        AutentificacaoController.getInstance().resetSenha(emailLogin.getText().toString(), LoginGui.this);
                    }
                })
                .setNegativeButton(LoginGui.this.getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void instanciaArtefatos() {
        mAuth = FirebaseAuth.getInstance();
        botaoLogin = (Button) findViewById(R.id.botaoLogin);

        //temporario

        botaoPaciente = (Button) findViewById(R.id.botaoPaciente);
        botaoDentista = (Button) findViewById(R.id.botaoDentista);

        botaoCadastro = (Button) findViewById(R.id.botaoCadastro);
        botaoRecuperar = (Button) findViewById(R.id.botaoEsqueciSenha);
        emailLogin = (EditText) findViewById(R.id.idEmailLogin);
        senhaLogin = (EditText) findViewById(R.id.idSenhaLogin);
        lembraEmail = (CheckBox) findViewById(R.id.idCheckBoxLembraEmail);
        emailLoginExterno = (TextInputLayout) findViewById(R.id.idEmailLoginExterno);
        senhaLoginExterno = (TextInputLayout) findViewById(R.id.idSenhaLoginExterno);
        editor = getSharedPreferences("Salva", MODE_PRIVATE).edit();
    }

    public void onBackPressed() {
        finish();
    }
}

