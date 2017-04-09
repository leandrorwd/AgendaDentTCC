package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.gui.paciente.Main_Paciente;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;
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
        activity.startActivity(new Intent(activity, Main_Paciente.class));
    }

    public UsuarioPaciente getPacienteLogado() {
        return pacienteLogado;
    }

    public void setPacienteLogado(UsuarioPaciente pacienteLogado) {
        this.pacienteLogado = pacienteLogado;
    }

    public void getPacienteConsultaDen(Consulta consulta, boolean banco, UsuarioPaciente user,Activity tela) {
        if(banco){
            TextView textoNomePaciente = (TextView) tela.findViewById(R.id.textoNomePaciente);
            TextView tipoConsulta = (TextView) tela.findViewById(R.id.tipoConsulta);
            TextView emailPaciente = (TextView) tela.findViewById(R.id.emailPaciente);
            TextView telefonePaciente = (TextView) tela.findViewById(R.id.telefonePaciente);
            textoNomePaciente.setText(user.getNome()+" "+ user.getSobrenome());
            emailPaciente.setText(user.getEmail());
            telefonePaciente.setText(user.getCelular());
            tipoConsulta.setText(consulta.getTipoConsulta());
        }
        else{
            pacienteBC.getPacienteConsultaDen(consulta,tela);
        }
    }
}
