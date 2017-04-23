package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AutenticacaoController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.LoginGui;
import tcc.agendadent.gui.dentista.Main_Dentista;
import tcc.agendadent.servicos.DialogAux;

import static tcc.agendadent.servicos.DialogAux.dialogCarregandoSimples;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteEditarPerfil extends LinearLayout implements ClassesPaciente {
    private Activity activity;
    private int id;
    private Button botaosalvar;
    private Button botaoAlterarSenha;
    private TextView textNome;
    private TextView textSobrenome;
    private TextView textSenha1;
    private TextView textSenha2;
    private TextView textCelular;

    public PacienteEditarPerfil(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_editar_perfil, this);
        carregaperfil();
        exibeJanela();
        instanciaArtefatos();
        setEventos();
        this.id = id_janela;
    }

    private void carregaperfil() {
        textNome = (TextView) PacienteEditarPerfil.this.findViewById(R.id.idAlterarNomePaciente);
        textSobrenome = (TextView) PacienteEditarPerfil.this.findViewById(R.id.idAlterarSobrenomePaciente);
//        textSenha1 = (TextView) PacienteEditarPerfil.this.findViewById(R.id.idAlterarSenhaPaciente);
//        textSenha2 = (TextView) PacienteEditarPerfil.this.findViewById(R.id.idAlterarSenhaPaciente2);
        textCelular = (TextView) PacienteEditarPerfil.this.findViewById(R.id.idAlterarCelularPaciente);

        textNome.setText(PacienteController.getInstance().getPacienteLogado().getNome());
        textSobrenome.setText(PacienteController.getInstance().getPacienteLogado().getSobrenome());
        textCelular.setText(PacienteController.getInstance().getPacienteLogado().getCelular());
    }

    private void exibeJanela() {
    }

    private void setEventos() {
        botaoAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoAlterarSenha();
            }
        });
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaosalvar();
            }
        });
    }

    private void botaoAlterarSenha() {
        dialogCarregandoSimples(activity);
        AutenticacaoController.getInstance().resetSenha(PacienteController.getInstance().getPacienteLogado().getEmail(), activity);
    }

    private void botaosalvar() {
        PacienteController.getInstance().getPacienteLogado().setNome(textNome.getText().toString());
        PacienteController.getInstance().getPacienteLogado().setSobrenome(textSobrenome.getText().toString());
        PacienteController.getInstance().getPacienteLogado().setCelular(textCelular.getText().toString());
        if (AutenticacaoController.getInstance().updatePerfilPaciente(PacienteController.getInstance().getPacienteLogado())) {
            DialogAux.dialogOkSimplesFinish(activity, "Confirmacão", "Dados alterados com sucesso! Por favor realize login novamente.");
        } else {
            DialogAux.dialogOkSimplesFinish(activity, "Erro", "Os dados não puderam ser atualizados!");
        }
    }


    @Override
    public void onResume() {
        exibeJanela();
    }

    @Override
    public boolean needResume() {
        return false;
    }

    @Override
    public int getIdMenu() {
        return id;
    }

    @Override
    public void flipper(boolean next) {

    }

    private void instanciaArtefatos() {
        botaoAlterarSenha = (Button) findViewById(R.id.botaoAlterarSenha);
        botaosalvar = (Button) findViewById(R.id.botaoSalvarPaciente);
    }
}
