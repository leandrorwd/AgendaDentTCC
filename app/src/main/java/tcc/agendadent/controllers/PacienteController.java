package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.widget.TextView;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.AgendaBC;
import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.gui.paciente.Main_Paciente;
import tcc.agendadent.gui.paciente.PacienteVisualizaDentistas;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.Utilitarios;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

/**
 * Created by natha on 09/02/2017.
 */

public class PacienteController {
    private static PacienteController INSTANCE;
    private PacienteBC pacienteBC;
    private UsuarioPaciente pacienteLogado;
    private ArrayList<UsuarioDentista> dentistas;


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

    public void getDentistasFiltro(Activity activity, String nomeDentista, String tipoConsulta, String planoSaude, String especializacao, Endereco endereco, int distanciaKm, double[] enderecoPaciente) {
        pacienteBC.getDentistasFiltro(activity,nomeDentista,tipoConsulta,planoSaude,especializacao,endereco,distanciaKm,enderecoPaciente);
    }

    public void filtraDentistas(Activity activity, String nomeDentista, String tipoConsulta,
                                String planoSaude, String especializacao,
                                Endereco endereco, int distanciaKm,
                                ArrayList<UsuarioDentista> listaDentistas, double[] enderecoPaciente) {

        ArrayList<UsuarioDentista> aux = new ArrayList<>();
        ArrayList<UsuarioDentista> retorno =listaDentistas;
        //Otimizar o codigo no futuro
        //region FiltraNome

        if(ValidationTest.validaString(nomeDentista)){
            aux.clear();
            for (UsuarioDentista user:retorno) {
                if(nomeDentista.contains(user.getNome()) || nomeDentista.contains(user.getSobreNome())){
                    aux.add(user);
                }
            }
            retorno = aux;
        }
        //endregion

        //region Filtra Tipo Consulta
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
            PacienteController.getInstance().setTipoConsultaConvenio(tipoConsulta,planoSaude);
            retorno.clear();
            retorno.addAll(aux);
        }
        //endregion

        //region Filtra Especializaçao
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

        //endregion

       // TODO Distancia do google maps api bla bla bla
        if(distanciaKm!=0){
            aux.clear();
            for (UsuarioDentista user:retorno) {
                double[] dentistaEnd = Utilitarios.getLatitudeLongitude(activity,user.getEndereco());

                Location loc1 = new Location("");
                loc1.setLatitude(enderecoPaciente[0]);
                loc1.setLongitude(enderecoPaciente[1]);

                Location loc2 = new Location("");
                loc2.setLatitude(dentistaEnd[0]);
                loc2.setLongitude(dentistaEnd[1]);
                float distanciaKmReal = loc1.distanceTo(loc2)/1000;
                if(distanciaKm>=distanciaKmReal){
                    aux.add(user);
                }
            }
            retorno.clear();
            retorno.addAll(aux);
        }

        //region Filtra Endereco Outro Local
        if(endereco!=null){
            aux.clear();
            for (UsuarioDentista user:retorno) {
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

        //endregion
        DialogAux.dialogCarregandoSimplesDismiss();
        if(retorno==null || retorno.size()==0){
            DialogAux.dialogOkSimples(activity, activity.getString(R.string.Aviso), activity.getString(R.string.nenhumDentistaEncontrado));
        }
        else{
            UsuarioDentista aux13 = retorno.get(0);
            setDentistas(retorno);
            activity.startActivity(new Intent(activity, PacienteVisualizaDentistas.class));
        }
    }
    private String tipoConsulta;
    private String planoSaude;
    private void setTipoConsultaConvenio(String tipoConsulta, String planoSaude) {
        this.tipoConsulta = tipoConsulta;
        this.planoSaude = planoSaude;
    }

    public ArrayList<UsuarioDentista> getDentistas() {
        return dentistas;
    }

    public void setDentistas(ArrayList<UsuarioDentista> dentistas) {
        this.dentistas = dentistas;
    }

    public UsuarioDentista getUsuarioDentistaMarcaConsulta() {
        return usuarioDentistaMarcaConsulta;
    }

    private UsuarioDentista usuarioDentistaMarcaConsulta;

    public void setDentistaMarcaConsulta(UsuarioDentista dentista) {
        usuarioDentistaMarcaConsulta=dentista;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public String getPlanoSaude() {
        return planoSaude;
    }
    private Horario horario;
    public void setHorarioConsulta(Horario horario) {
        this.horario = horario;
    }

    public Horario getHorario() {
        return horario;
    }

    public String getHoraConsultaInicial() {
        return horaConsultaInicial;
    }

    private String horaConsultaInicial;
    public void setHoraConsultaInicial(String horaConsultaInicial) {
        this.horaConsultaInicial = horaConsultaInicial;
    }

    public void salvaEndereco(Activity activity, String cep, String estado, String cidade,
                              String bairro, String rua, String numero, String complemento) {
        String erro = activity.getResources().getString(R.string.erro);

        if(!ValidationTest.verificaInternet(activity)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.internetSemConexao));
            return;
        }
        if(!ValidationTest.validaString(cep)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.cepVazio));
            return;
        }

        if(!ValidationTest.validaCep(cep)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.cepInvalido));
            return;
        }

        if(!ValidationTest.validaString(estado)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.estadoVazio));
            return;
        }
        if(!ValidationTest.validaString(cidade)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.cidadeVazio));
            return;
        }
        if(!ValidationTest.validaString(bairro)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.bairroVazio));
            return;
        }
        if(!ValidationTest.validaString(rua)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.ruaVazio));
            return;
        }
        if(!ValidationTest.validaString(numero)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.numeroVazio));
            return;
        }
        if(!ValidationTest.validaNumeroPuro(numero)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.numeroInvalido));
            return;
        }
        String cepAux = cep.replace("-","");
        Endereco e1 = new Endereco("Brasil",estado,cidade,bairro,rua,complemento,
                Integer.parseInt(numero),Integer.parseInt(cepAux));

        pacienteBC.atualizaEndereco(activity,e1);
    }
}
