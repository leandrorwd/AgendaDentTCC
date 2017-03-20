package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;

import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.gui.paciente.MenuPaciente;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by natha on 09/02/2017.
 */

public class PacienteController {
    private static PacienteController INSTANCE;
    private PacienteBC pacienteBC;
    private UsuarioPaciente pacienteLogado;


    private PacienteController() {
        pacienteBC = new PacienteBC();
    }

    public static PacienteController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PacienteController();
        }
        return INSTANCE;
    }

    public void setUsuarioAtualLogin(UsuarioPaciente paciente, Activity activity) {
        pacienteLogado = paciente;
        DialogAux.dialogCarregandoSimplesDismiss();
        activity.startActivity(new Intent(activity, MenuPaciente.class));
    }

    public UsuarioPaciente getPacienteLogado() {
        return pacienteLogado;
    }

    public void setPacienteLogado(UsuarioPaciente pacienteLogado) {
        this.pacienteLogado = pacienteLogado;
    }
}
