package tcc.agendadent.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.DentistaBC;
import tcc.agendadent.gui.dentista.Main_Dentista;
import tcc.agendadent.gui.layout_auxiliares.TemplateCardHorario;
import tcc.agendadent.objetos.Agenda;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.DownloadImageTask;
import tcc.agendadent.servicos.Utilitarios;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

/**
 * Created by natha on 09/02/2017.
 */

public class DentistaController {

    private static DentistaController INSTANCE;
    private DentistaBC dentistaBC;
    private UsuarioDentista dentistaLogado;
    private boolean horarioInicio;
    private boolean horarioTermino;
    private boolean horarioDuracao;
    private ArrayList<Horario> horariosTemporarios;
    private Activity telaAuxiliar;

    private DentistaController() {
        dentistaBC = new DentistaBC();
        horariosTemporarios = new ArrayList<>();
    }

    public void getDentistasBC() {
        dentistaBC.getTodosDentistas();
    }

    public void atualizaDentistas(ArrayList<UsuarioDentista> dentistas) {
        AgendaController.getInstance().buscaAgendaBCAgendadas(dentistas);
    }

    public void getDentistasBCHistorico() {
        dentistaBC.getTodosDentistasHistorico();
    }

    public void atualizaDentistasHistorico(ArrayList<UsuarioDentista> dentistas) {
        AgendaController.getInstance().buscaAgendaBCHistorico(dentistas);
    }

    public static DentistaController getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DentistaController();
        }
        return INSTANCE;
    }

    public void setUsuarioAtualLogin(UsuarioDentista dentista, Activity activity) {
        dentistaLogado = dentista;
        DialogAux.dialogCarregandoSimplesDismiss();
        activity.startActivity(new Intent(activity, Main_Dentista.class));
    }

    public boolean isHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(boolean horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public boolean isHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(boolean horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public Activity getTelaAuxiliar() {
        return telaAuxiliar;
    }

    public void setTelaAuxiliar(Activity telaAuxiliar) {
        this.telaAuxiliar = telaAuxiliar;
    }

    public boolean isHorarioDuracao() {
        return horarioDuracao;
    }

    public void setHorarioDuracao(boolean horarioDuracao) {
        this.horarioDuracao = horarioDuracao;
    }

    public boolean verificaHorario(Horario horarioNovo) {
        for (Horario h : getAgenda().getHorarios()){
            int i = 0;
            while(i<7){
                if((horarioNovo.getDiasSemana().get(i)==h.getDiasSemana().get(i)) && horarioNovo.getDiasSemana().get(i))
                {
                    String[] parts =  horarioNovo.getHoraInicial().split(":");
                    int horaInicial = Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);

                    String[] parts2 =  horarioNovo.getHoraFinal().split(":");
                    int horaFinal = Integer.parseInt(parts2[0])*60+Integer.parseInt(parts2[1]);

                    String[] parts3 =  h.getHoraInicial().split(":");
                    int hInicial = Integer.parseInt(parts3[0])*60+Integer.parseInt(parts3[1]);

                    String[] parts4 =  h.getHoraFinal().split(":");
                    int hFInal = Integer.parseInt(parts4[0])*60+Integer.parseInt(parts4[1]);

                    if(horaInicial> hInicial && horaInicial< hFInal){
                        return false;
                    }
                    if(horaFinal> hInicial && horaFinal< hFInal){
                        return false;
                    }
                }
                i++;
            }
        }
        return true;
    }

    public Agenda getAgenda(){
        return dentistaLogado.getAgenda();
    }

    public void addHorarioAgenda(Horario horarioNovo,Activity tela) {
        horariosTemporarios.add(horarioNovo);
        getAgenda().addHorario(horarioNovo);
        tela.finish();
    }

    public ArrayList<Horario> getHorariosTemporarios() {
        return horariosTemporarios;
    }

    public void setDentista(Activity activity, UsuarioDentista usuario,boolean bancoChamada){
        if(bancoChamada){
            DialogAux.dialogCarregandoSimplesDismiss();
            DialogAux.dialogOkSimples(activity,activity.getResources().getString(R.string.Sucesso),activity.getResources().getString(R.string.dadosSalvosSucesso));
        }
        else{
            DialogAux.dialogCarregandoSimples(activity);
            dentistaBC.setDentista(activity,usuario);
        }
    }

    public UsuarioDentista getDentistaLogado() {
        return dentistaLogado;
    }

    public void setDentistaLogado(UsuarioDentista dentistaLogado) {
        this.dentistaLogado = dentistaLogado;
    }

    public void carregaHorarios(Activity activity, UsuarioDentista dentistaLogado, boolean banco) {
        if(banco){
            LinearLayout main =(LinearLayout)activity.findViewById(R.id.layoutPrincipalConfigAgenda);
            main.removeAllViews();
            for(Horario h : DentistaController.getInstance().getAgenda().getHorarios()){
                TemplateCardHorario t1 = new TemplateCardHorario(activity,activity,h);
                main.addView(t1);
            }
            if(DentistaController.getInstance().getHorariosTemporarios()!=null)
            for(Horario h : DentistaController.getInstance().getHorariosTemporarios()){
                TemplateCardHorario t1 = new TemplateCardHorario(activity,activity,h);
                main.addView(t1);
            }
            DialogAux.dialogCarregandoSimplesDismiss();
        }
        else{
           DialogAux.dialogCarregandoSimples(activity);
           dentistaBC.getHorarios(activity,dentistaLogado);
        }
    }

    public void salvaPerfilDentista(Activity activity,  String nome, String sobreNome,
                                 String inscricaoCro, String cep, String estado, String cidade,
                                 String bairro, String rua, String numero, String complemento,
                                 ImageView perfil, String celular) {
        String erro = activity.getResources().getString(R.string.erro);

        if(!ValidationTest.verificaInternet(activity)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.internetSemConexao));
            return;
        }
        if(!ValidationTest.validaString(nome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.nomeVazio));
            return;
        }
        if(!ValidationTest.validaString(sobreNome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.sobreNomeVazio));
            return;
        }


        if(!ValidationTest.validaString(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celularVazio));
            return;
        }


        if(!ValidationTest.validaTelefone(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celulaInvalido));
            return;
        }
        if(!ValidationTest.validaString(inscricaoCro)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.inscricaoCROVazio));
            return;
        }
        //TODO Validacao CORRETA do InscricaoCRO
        boolean upaFoto = true;
        if(!ValidationTest.validaFotoTirada(perfil)){
            upaFoto = false;
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
        setFotoPerfil(perfil);
        DentistaController.getInstance().setFotoPerfil(perfil);
        DentistaController.getInstance().getDentistaLogado().setEndereco(e1);
        DentistaController.getInstance().getDentistaLogado().setNome(nome);
        DentistaController.getInstance().getDentistaLogado().setSobreNome(sobreNome);
        DentistaController.getInstance().getDentistaLogado().setTelefone(celular);
        DentistaController.getInstance().getDentistaLogado().setInscricaoCRO(inscricaoCro);
        DentistaController.getInstance().getDentistaLogado().setEndereco(e1);
        DentistaController.getInstance().atualizaDentista(activity,DentistaController.getInstance().getDentistaLogado(),upaFoto);
    }
    public void setImagemPerfilDentista(Bitmap foto, Activity tela) {
        ImageView perfil = (ImageView) tela.findViewById(R.id.fotoTela);
        perfil.setImageBitmap(foto);
    }
    private void atualizaDentista(Activity activity, UsuarioDentista dentistaLogado, boolean upaFoto) {
        dentistaBC.atualizaDentista(dentistaLogado,activity,upaFoto);
    }

    public Bitmap getFotoPerfilCadastro() {
        return fotoPerfilCadastro;
    }

    public void setFotoPerfilCadastro(Bitmap fotoPerfilCadastro) {
        this.fotoPerfilCadastro = fotoPerfilCadastro;
    }

    private Bitmap fotoPerfilCadastro;

    private void setFotoPerfil(ImageView perfil) {
        Bitmap bitmapPerfil = ((BitmapDrawable)perfil.getDrawable()).getBitmap();
        fotoPerfilCadastro = Utilitarios.getResizedBitmap(bitmapPerfil,500);
    }

    public void setImagemPerfilLoading(Activity activity, String urlFotoPerfil, ImageView fotoTela) {
        Uri download = Uri.parse(urlFotoPerfil);
        new DownloadImageTask(fotoTela,activity)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, download.toString());
    }



}
