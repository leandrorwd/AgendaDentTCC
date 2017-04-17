package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.AgendaBC;
import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.gui.layout_auxiliares.TemplatePacienteConsultasAgendadas;
import tcc.agendadent.gui.paciente.Main_Paciente;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

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

    public void getDentistasFiltro(Activity activity, String nomeDentista, String tipoConsulta, String planoSaude, String especializacao, Endereco endereco, int distanciaKm) {
        pacienteBC.getDentistasFiltro(activity,nomeDentista,tipoConsulta,planoSaude,especializacao,endereco,distanciaKm);
    }

    public void filtraDentistas(Activity activity, String nomeDentista, String tipoConsulta,
                                String planoSaude, String especializacao,
                                Endereco endereco, int distanciaKm,
                                ArrayList<UsuarioDentista> listaDentistas) {
        ArrayList<UsuarioDentista> aux = new ArrayList<>();
        ArrayList<UsuarioDentista> retorno =listaDentistas;

        if(ValidationTest.validaString(nomeDentista)){
            aux.clear();
            for (UsuarioDentista user:retorno) {
                if(nomeDentista.contains(user.getNome()) || nomeDentista.contains(user.getSobreNome())){
                    aux.add(user);
                }
            }
            retorno = aux;
        }
        //TODO
        if(ValidationTest.validaString(tipoConsulta)){
            aux.clear();
            for (UsuarioDentista user:retorno) {
                if(tipoConsulta.equals("Particular")){
                    aux.add(user);
                    continue;
                }
                if(tipoConsulta.equals("Convênio")){
                    if(planoSaude.equals("Selecione um plano de saúde:")){
                        DialogAux.dialogOkSimples(activity,activity.getResources().getString(R.string.erro)
                                ,activity.getResources().getString(R.string.missplan));
                        return;
                    }
                    if(planoSaude.equals("Amil Dental") && user.getConvenios().isAmilDental()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Bello Dente") && user.getConvenios().isBelloDente()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Bradesco Dental") && user.getConvenios().isBradesco()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Doctor Clin") && user.getConvenios().isDoctorClin()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Interodonto") && user.getConvenios().isInterodonto()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Metlife") && user.getConvenios().isMetlife()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Odonto Empresas") && user.getConvenios().isOdontoEmpresa()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("SulAmerica Odonto") && user.getConvenios().isSulAmerica()){
                        aux.add(user);
                        continue;
                    }
                    if(planoSaude.equals("Uniodonto") && user.getConvenios().isUniOdonto()){
                        aux.add(user);
                        continue;
                    }

                }
                if(tipoConsulta.equals("SUS") && user.getConvenios().isSus()){
                    aux.add(user);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);
        }
        if(ValidationTest.validaString(especializacao)){
            aux.clear();
            for (UsuarioDentista user:retorno) {
                if(user.getEspecializacoes().isClinicoGeral() && especializacao.equals("Clinico Geral"))
                    aux.add(user);
                if(user.getEspecializacoes().isEndodontia() && especializacao.equals("Endodontia"))
                    aux.add(user);
                if(user.getEspecializacoes().isImplantodontia() && especializacao.equals("Implantodontia"))
                    aux.add(user);
                if(user.getEspecializacoes().isOrtodontia() && especializacao.equals("Ortodontia"))
                    aux.add(user);
                if(user.getEspecializacoes().isOdontopediatria() && especializacao.equals("Odontopediatria"))
                    aux.add(user);
                if(user.getEspecializacoes().isOdontologiaEstetica() && especializacao.equals("Odontologia Estética"))
                    aux.add(user);
                if(user.getEspecializacoes().isProtese() && especializacao.equals("Protese"))
                    aux.add(user);
            }
            retorno.clear();
            retorno.addAll(aux);
        }
        //TODO Refatorar esse codigo, arrumar bugs de campos obrigatorios,se nao tiver radiobutton clicado ignorar todo o resto.
        //        if(distanciaKm!=0){
//            aux.clear();
//            for (UsuarioDentista user:retorno) {
//
//            }
//            retorno = aux;
//        }
        if(endereco!=null){
            aux.clear();
            for (UsuarioDentista user:retorno) {
                if(endereco.getEstado().equals("")){
                    DialogAux.dialogOkSimples(activity,activity.getResources().getString(R.string.erro)
                            ,activity.getResources().getString(R.string.missState));
                    return;
                }
                if((endereco.getEstado()!=null && !endereco.getEstado().equals(""))){
                    if(endereco.getEstado().equals(user.getEndereco().getEstado())){
                        aux.add(user);
                    }
                }
                else{
                    aux.addAll(retorno);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);

            aux.clear();
            for (UsuarioDentista user:retorno) {
                if((endereco.getCidade()!=null && !endereco.getCidade().equals(""))){
                    if(user.getEndereco().getCidade().contains(endereco.getCidade())){
                        aux.add(user);
                    }
                }
                else{
                    aux.addAll(retorno);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);

            aux.clear();
            for (UsuarioDentista user:retorno) {
                if((endereco.getBairro()!=null && !endereco.getBairro().equals(""))){
                    if(user.getEndereco().getBairro().contains(endereco.getBairro())){
                        aux.add(user);
                    }
                }
                else{
                    aux.addAll(retorno);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);

            aux.clear();
            for (UsuarioDentista user:retorno) {
                if((endereco.getRua()!=null && !endereco.getRua().equals(""))){
                    if(user.getEndereco().getRua().contains(endereco.getRua())){
                        aux.add(user);
                    }
                }
                else{
                    aux.addAll(retorno);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);

            aux.clear();
            for (UsuarioDentista user:retorno) {
                if(endereco.getNumero()!=0){
                    if(user.getEndereco().getNumero()==(endereco.getNumero())){
                        aux.add(user);
                    }
                }
                else{
                    aux.addAll(retorno);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);

            aux.clear();
            for (UsuarioDentista user:retorno) {
                if((endereco.getComplemento()!=null && !endereco.getComplemento().equals(""))){
                    if(user.getEndereco().getComplemento().contains(endereco.getComplemento())){
                        aux.add(user);
                    }
                }
                else{
                    aux.addAll(retorno);
                    break;
                }
            }
            retorno.clear();
            retorno.addAll(aux);
        }

        Toast.makeText(activity, retorno.get(0).getNome(), Toast.LENGTH_SHORT).show();
    }
}
