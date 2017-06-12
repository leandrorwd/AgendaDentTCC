package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;

import static java.lang.Integer.parseInt;

public class PacienteMarcaConsulta extends AppCompatActivity {


    private static Activity activity;
    private static UsuarioDentista dentista;
    private CardView layout;
    private CardView escolha;
    private TextView nome;
    private TextView nota;
    private TextView especialidades;
    private TextView email;
    private TextView telefone;
    private TextView data;
    private TextView hora;
    private TextView tipoConsulta;
    private TextView endereco;
    private Button botaoSalvar;
    private Button cancelar;
    private KillReceiver mKillReceiver;
    private static Consulta consultaNotificao;
    private ImageView fotoPerfil;
    private static Spinner tipoConsultaSpinner;
    private static Spinner planosDeSaude;
    private static Consulta consultaDesmarcar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_marca_consulta);
        if(PacienteController.getInstance().getConsultaNotificao()==null){
            dentista = PacienteController.getInstance().getUsuarioDentistaMarcaConsulta();
            instanciaArtefatos();
            setEventos();
            mKillReceiver = new KillReceiver();
            registerReceiver(mKillReceiver, IntentFilter.create("kill", "text/plain"));
        }
        else{
            activity = PacienteMarcaConsulta.this;
            DialogAux.dialogCarregandoSimples(PacienteMarcaConsulta.this);
            consultaNotificao = PacienteController.getInstance().getConsultaNotificao();
            if(PacienteController.getInstance().getConsultaDesmarc()!=null){
                consultaDesmarcar = PacienteController.getInstance().getConsultaDesmarc();
            }
            PacienteController.getInstance().getDentistaViaId(activity,consultaNotificao.getIdDentista(),this);
            PacienteController.getInstance().setConsultaNotificao(null);
            PacienteController.getInstance().setConsultaNotificaoDesmarc(null);
        }


    }

    public void instanciaArtefatosNotificacao(UsuarioDentista usuarioDentista) {

        dentista = usuarioDentista;
        setDuracao();
        fotoPerfil = (ImageView) findViewById(R.id.fotoDentistaImageView);
        nome = (TextView) findViewById(R.id.textViewNome);
        escolha = (CardView) findViewById(R.id.cardViewEscolha);
        nota = (TextView) findViewById(R.id.nota);
        especialidades = (TextView) findViewById(R.id.especialidades);
        endereco = (TextView) findViewById(R.id.endereco);
        email = (TextView) findViewById(R.id.email);
        telefone = (TextView) findViewById(R.id.telefone);
        data = (TextView) findViewById(R.id.data);
        hora = (TextView) findViewById(R.id.hora);
        email = (TextView) findViewById(R.id.email);
        tipoConsulta = (TextView) findViewById(R.id.tipoConsulta);
        botaoSalvar = (Button) findViewById(R.id.botaoSalvar);
        cancelar =(Button) findViewById(R.id.botaCancela);
        layout = (CardView) findViewById(R.id.card_view);
        nome.setText(dentista.getNomeCompleto());
        nota.setText("5.0");
        especialidades.setText(dentista.getEspecializacoesString());
        endereco.setText("Endereço: "+dentista.getEndereco().toString());
        email.setText("Email: "+dentista.getEmail());
        telefone.setText("Telefone: "+dentista.getTelefone());
        tipoConsultaSpinner = (Spinner) findViewById(R.id.tipoSpinnerConsulta);
        planosDeSaude = (Spinner) findViewById(R.id.idPlanosDeSaude);
        planosDeSaude.setVisibility(View.GONE);

        // tipoConsulta.setText("Tipo consulta: "+ consultaNotificao.getTipoConsulta());
        tipoConsulta.setVisibility(View.GONE);
        DateTime aux = new DateTime(consultaNotificao.getDataConsultaPrimaria());
        DateTime aux2 = new DateTime(consultaNotificao.getDataConsultaPrimaria()+consultaNotificao.getDuracao());

        data.setText("Data: "+aux.getDayOfMonth()+"/"+
                aux.getMonthOfYear()+"/"+
                aux.getYear());


        DateTime auxas = DateTime.now();
        int horas = aux.getHourOfDay();
        int mins = aux.getMinuteOfHour();
        try {
            String operador = auxas.toString().substring(23, 26);
            int valor = parseInt(auxas.toString().substring(24, 26));
            if (operador.contains("+")) {
                horas = horas + valor;
            } else {
                horas = horas - valor;
            }
        } catch (Exception e) {

        }
        DateTime date0 = new DateTime(aux.getYear(),
                aux.getMonthOfYear(),
                aux.getDayOfMonth(), horas, mins);
        int duracao = duracaoMilis(horarioAux.getDuracao());
        consultaNotificao.setDuracao(duracao);
        DateTime date1 = date0.plusMillis(duracao);
        String date0Min,date1Min;
        if(date0.getMinuteOfHour()<10){
            date0Min = "0"+date0.getMinuteOfHour();
        }
        else{
            date0Min = date0.getMinuteOfHour()+"";
        }
        if(date1.getMinuteOfHour()<10){
            date1Min = "0"+date1.getMinuteOfHour();
        }
        else{
            date1Min = date1.getMinuteOfHour()+"";
        }
        hora.setText("Hora: "+date0.getHourOfDay() +":" +date0Min + " - "+date1.getHourOfDay() +":" +date1Min);
        DentistaController.getInstance().setImagemPerfilLoading(PacienteMarcaConsulta.this,dentista.getUrlFotoPerfil(),fotoPerfil);
        DialogAux.dialogCarregandoSimplesDismiss();
        setEventosNotificacao();
    }

    private int duracaoMilis(String duracao) {
        String[] parts = duracao.split(":");
        int hora = Integer.parseInt(parts[0]) * 60;
        int min = Integer.parseInt(parts[1]);
        return (hora+min)*60000;
    }

    private void setEventosNotificacao() {

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgendaController.getInstance().checkConsultaLivre(consultaNotificao);
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarPacienteMarcaConsulta));
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        tipoConsultaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (tipoConsultaSpinner.getSelectedItem().toString().equals("Convênio")) {
                    planosDeSaude.setVisibility(View.VISIBLE);
                } else
                    planosDeSaude.setVisibility(View.GONE);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private static boolean checkTipoConsulta() {
        String planoSaude;
        if (!tipoConsultaSpinner.getSelectedItem().toString().equals("Convênio")) {
            planoSaude = "null";
        } else {
            planoSaude = planosDeSaude.getSelectedItem().toString();
        }
        if(horarioAux.isConvenio()) {
            if (tipoConsultaSpinner.equals("Convênio")) {
                if (planoSaude.equals("Selecione um plano de saúde:")) {
                    DialogAux.dialogOkSimples(activity, activity.getResources().getString(R.string.erro)
                            , activity.getResources().getString(R.string.missplan));
                    return false;
                }
                if (planoSaude.equals("Amil Dental") && dentista.getConvenios().isAmilDental()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Bello Dente") && dentista.getConvenios().isBelloDente()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Bradesco Dental") && dentista.getConvenios().isBradesco()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Doctor Clin") && dentista.getConvenios().isDoctorClin()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Interodonto") && dentista.getConvenios().isInterodonto()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Metlife") && dentista.getConvenios().isMetlife()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Odonto Empresas") && dentista.getConvenios().isOdontoEmpresa()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("SulAmerica Odonto") && dentista.getConvenios().isSulAmerica()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }
                if (planoSaude.equals("Uniodonto") && dentista.getConvenios().isUniOdonto()) {
                    consultaNotificao.setTipoConsulta("Convênio - "+ planoSaude);
                    return true;
                }

            }
        }

        if(tipoConsultaSpinner.getSelectedItem().toString().equals("Particular") && horarioAux.isParticular()){
            consultaNotificao.setTipoConsulta("Particular");
            return true;
        }
        if(tipoConsultaSpinner.getSelectedItem().toString().equals("SUS") && horarioAux.isSus()){
            consultaNotificao.setTipoConsulta("SUS");
            return true;
        }
        return false;
    }

    private static Horario horarioAux;
    private void setDuracao() {
        DateTime aux = new DateTime(consultaNotificao.getDataConsultaPrimaria());
        DateTime auxas = DateTime.now();
        int horas = aux.getHourOfDay();
        int mins = aux.getMinuteOfHour();
        try {
            String operador = auxas.toString().substring(23, 26);
            int valor = parseInt(auxas.toString().substring(24, 26));
            if (operador.contains("+")) {
                horas = horas + valor;
            } else {
                horas = horas - valor;
            }
        } catch (Exception e) {

        }
        DateTime date0 = new DateTime(aux.getYear(),
                aux.getMonthOfYear(),
                aux.getDayOfMonth(), horas, mins);
        for (Horario h : dentista.getAgenda().getHorarios()){
            if(h.getDiasSemana().get(consultaNotificao.getDataFormat().getDayOfWeek()-1)){
                String[] horaInicial =h.getHoraInicial().split(":");
                int horasInicial = parseInt(horaInicial[0]);
                String[] horasFinais =h.getHoraInicial().split(":");
                int horaFInal = parseInt(horasFinais[0]);
                if(horasInicial<=date0.getHourOfDay()){
                    if(horaFInal>=date0.getHourOfDay()){
                        horarioAux = h;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(consultaNotificao!=null){
            consultaNotificao = null;
        }
        else{
            unregisterReceiver(mKillReceiver);
        }
    }

    public static void dialogErro() {
        DialogAux.dialogOkSimples(activity,"Erro","Este horario já foi selecionado por outra pessoa.");
    }

    private final class KillReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    private void setEventos() {
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAux.dialogCarregandoSimples(PacienteMarcaConsulta.this);
                String[] parts = PacienteController.getInstance().getHoraConsultaInicial().split(":");
                DateTime dateDataConsultaInicial = new DateTime(AgendaController.getInstance().getMomento().getYear(),
                        AgendaController.getInstance().getMomento().getMonthOfYear(),
                        AgendaController.getInstance().getMomento().getDayOfMonth(),
                        parseInt(parts[0]),
                        parseInt(parts[1]), 0, 0);
                String[] parts2 = PacienteController.getInstance().getHorario().getDuracao().split(":");

                long valor =((parseInt(parts2[0])*60) + parseInt(parts2[1]))*60000 ;
                String tipoConsultaString="";
                if(PacienteController.getInstance().getTipoConsulta().equals("Convênio")){
                    tipoConsultaString=PacienteController.getInstance().getTipoConsulta()+ " - "
                            +(PacienteController.getInstance().getPlanoSaude());
                }
                else{
                    tipoConsultaString =PacienteController.getInstance().getTipoConsulta();
                }
                int mes = DateTime.now().monthOfYear().get();
                String anoSemestre;
                if(mes>=7){
                    anoSemestre =dateDataConsultaInicial.year().get() +"A2";
                }
                else
                    anoSemestre =dateDataConsultaInicial.year().get() +"A1";
                anoSemestre = anoSemestre.replace("A","");
                long valorAux = 0;
                try{
                    String operador = DateTime.now().toString().substring(23,26);
                    valorAux = parseInt( DateTime.now().toString().substring(24,26));
                    if(operador.contains("+")){
                        valorAux = valorAux * 3600000;
                    }
                    else{
                        valorAux = (valorAux * 3600000) - (valorAux * 3600000)*2;
                     }
                }catch (Exception e){
                    valorAux =0;
                }
                String s = dateDataConsultaInicial.getMillis()+"";
                String s2 = valorAux+"";
                Consulta c1 = new Consulta(PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getIdDentista()
                        , PacienteController.getInstance().getPacienteLogado().getIdPaciente(),
                        dateDataConsultaInicial.getMillis()+valorAux
                        ,5, tipoConsultaString,PacienteController.getInstance().getPacienteLogado().getNome(),valor, false);
                CheckBox check = (CheckBox) findViewById(R.id.checkboxRemarcar);
                c1.setTentarRemarcar(check.isChecked());
                AgendaController.getInstance().insertConsulta(PacienteMarcaConsulta.this,c1,
                        PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getIdDentista()+"",anoSemestre);
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarPacienteMarcaConsulta));
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void salvar(){
        DialogAux.dialogCarregandoSimples(activity);
        if(!checkTipoConsulta()){
            DialogAux.dialogOkSimples(activity,"Erro","Não é possivel selecionar este tipo de consulta.");
            return;
        }
        consultaNotificao.setIdPaciente(PacienteController.getInstance().getPacienteLogado().getIdPaciente());
        int mes = DateTime.now().monthOfYear().get();
        String anoSemestre;
        if(mes>=7){
            anoSemestre = consultaNotificao.getDataFormat().year().get() +"A2";
        }
        else
            anoSemestre = consultaNotificao.getDataFormat().year().get() +"A1";
        anoSemestre = anoSemestre.replace("A","");
        AgendaController.getInstance().insertConsulta(activity,consultaNotificao,
                consultaNotificao.getIdDentista()+"",anoSemestre,true);
//        if(consultaDesmarcar!=null){
//            AgendaController.getInstance().desmarcarConsulta(activity,consultaDesmarcar);
//        }
    }

    private void instanciaArtefatos() {
        fotoPerfil = (ImageView) findViewById(R.id.fotoDentistaImageView);
        escolha = (CardView) findViewById(R.id.cardViewEscolha);
        escolha.setVisibility(View.GONE);
        nome = (TextView) findViewById(R.id.textViewNome);
        nota = (TextView) findViewById(R.id.nota);
        especialidades = (TextView) findViewById(R.id.especialidades);
        endereco = (TextView) findViewById(R.id.endereco);
        email = (TextView) findViewById(R.id.email);
        telefone = (TextView) findViewById(R.id.telefone);
        data = (TextView) findViewById(R.id.data);
        hora = (TextView) findViewById(R.id.hora);
        email = (TextView) findViewById(R.id.email);
        tipoConsulta = (TextView) findViewById(R.id.tipoConsulta);
        botaoSalvar = (Button) findViewById(R.id.botaoSalvar);
        cancelar =(Button) findViewById(R.id.botaCancela);
        layout = (CardView) findViewById(R.id.card_view);
        nome.setText(dentista.getNomeCompleto());
        nota.setText("5.0");
        especialidades.setText(dentista.getEspecializacoesString());
        endereco.setText("Endereço: "+dentista.getEndereco().toString());
        email.setText("Email: "+dentista.getEmail());
        telefone.setText("Telefone: "+dentista.getTelefone());
        if(PacienteController.getInstance().getTipoConsulta().equals("Convênio")){
            tipoConsulta.setText("Tipo consulta: "+PacienteController.getInstance().getTipoConsulta()+ " - "
            +(PacienteController.getInstance().getPlanoSaude()));
        }
        else{
            tipoConsulta.setText("Tipo consulta: "+PacienteController.getInstance().getTipoConsulta());
        }

        data.setText("Data: "+AgendaController.getInstance().getMomento().getDayOfMonth()+"/"+
                AgendaController.getInstance().getMomento().getMonthOfYear()+"/"+
                AgendaController.getInstance().getMomento().getYear());
        //region getHoraFinal
        String[] parts = PacienteController.getInstance().getHorario().getDuracao().split(":");
        String[] parts2 = PacienteController.getInstance().getHoraConsultaInicial().split(":");
        int novaHora = (parseInt(parts[0]) + parseInt(parts2[0]));
        int novoMinuto = parseInt(parts[1]) + parseInt(parts2[1]);

        if (novoMinuto >= 60) {
            novoMinuto = novoMinuto - 60;
            novaHora = novaHora + 1;
        }
        String snovaHora = novaHora + "";
        String snovoMinuto = novoMinuto + "";
        if (novaHora < 10) {
            snovaHora = "0" + novaHora;
        }
        if (novoMinuto < 10) {
            snovoMinuto = "0" + novoMinuto;
        }
        String horaFinal = snovaHora + ":" + snovoMinuto;
        //endregion
        hora.setText("Hora: "+PacienteController.getInstance().getHoraConsultaInicial() + " - "+horaFinal);
        DentistaController.getInstance().setImagemPerfilLoading(PacienteMarcaConsulta.this,dentista.getUrlFotoPerfil(),fotoPerfil);
    }
}
