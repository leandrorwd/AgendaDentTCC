package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;

public class PacienteMarcaConsulta extends AppCompatActivity {

    private Activity activity;
    private UsuarioDentista dentista;
    private CardView layout;
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

    private ImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_marca_consulta);
        dentista = PacienteController.getInstance().getUsuarioDentistaMarcaConsulta();
        instanciaArtefatos();
        setEventos();
    }

    private void setEventos() {
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parts = PacienteController.getInstance().getHoraConsultaInicial().split(":");
                DateTime dateDataConsultaInicial = new DateTime(AgendaController.getInstance().getMomento().getYear(),
                        AgendaController.getInstance().getMomento().getMonthOfYear(),
                        AgendaController.getInstance().getMomento().getDayOfMonth(),
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]), 0, 0);
                String[] parts2 = PacienteController.getInstance().getHorario().getDuracao().split(":");
                long valor =((Integer.parseInt(parts[0])*60) +Integer.parseInt(parts[1]))*60000 ;
                String tipoConsultaString="";
                if(PacienteController.getInstance().getTipoConsulta().equals("Convênio")){
                    tipoConsultaString=PacienteController.getInstance().getTipoConsulta()+ " - "
                            +(PacienteController.getInstance().getPlanoSaude());
                }
                else{
                    tipoConsultaString ="Tipo consulta: "+PacienteController.getInstance().getTipoConsulta();
                }
                int mes = DateTime.now().monthOfYear().get();
                String anoSemestre= DateTime.now().year().get()+"";
                if(mes>=7){
                    anoSemestre = DateTime.now().year().get() +"A2";
                }
                else
                    anoSemestre = DateTime.now().year().get() +"A1";
                anoSemestre = anoSemestre.replace("A","");
                Consulta c1 = new Consulta(PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getIdDentista()
                        , PacienteController.getInstance().getPacienteLogado().getIdPaciente(),
                        dateDataConsultaInicial.getMillis()
                        ,5, tipoConsultaString,PacienteController.getInstance().getPacienteLogado().getNome(),valor);
                AgendaController.getInstance().insertConsulta(c1,
                        PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getIdDentista()+"",anoSemestre);
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

    }


    private void instanciaArtefatos() {
        fotoPerfil = (ImageView) findViewById(R.id.fotoDentistaImageView);
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
        int novaHora = (Integer.parseInt(parts[0]) + Integer.parseInt(parts2[0]));
        int novoMinuto = Integer.parseInt(parts[1]) + Integer.parseInt(parts2[1]);

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
