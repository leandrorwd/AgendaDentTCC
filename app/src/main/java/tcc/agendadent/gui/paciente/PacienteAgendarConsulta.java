package tcc.agendadent.gui.paciente;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import tcc.agendadent.R;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Endereco;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.Utilitarios;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Leandro on 25/03/2017.
 */

public class PacienteAgendarConsulta extends LinearLayout implements ClassesPaciente {
    private Activity activity;
    private Button botaopesquisar;
    private int id;
    private EditText nomeDentistaBusca;
    private Spinner tipoConsulta;
    private Spinner planosDeSaude;
    private Spinner especializacao;
    private Spinner spinnerEstado;
    private RadioGroup radioGroup;
    private LinearLayout proximoMim;
    private LinearLayout outroEndereco;
    private EditText cidadeDentista;
    private EditText bairroDentista;
    private EditText ruaDentista;
    private EditText numeroDentista;
    private EditText complementoDentista;
    private Button pesquisar;
    private EditText  distanciaKm;
    public String titulo = "Agendar Consulta";

    public PacienteAgendarConsulta(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.paciente_busca_dentista, this);
        this.id = id_janela;
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        nomeDentistaBusca = (EditText) findViewById(R.id.nomeDentistaBusca);
        cidadeDentista = (EditText) findViewById(R.id.cidadeDentista);
        bairroDentista = (EditText) findViewById(R.id.bairroDentista);
        ruaDentista = (EditText) findViewById(R.id.ruaDentista);
        numeroDentista = (EditText) findViewById(R.id.numeroDentista);
        complementoDentista = (EditText) findViewById(R.id.complementoDentista);
        distanciaKm = (EditText) findViewById(R.id.distanciaKm);

        outroEndereco = (LinearLayout) findViewById(R.id.layoutOutroEndereco);
        proximoMim = (LinearLayout) findViewById(R.id.layoutProximoMim);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        tipoConsulta = (Spinner) findViewById(R.id.tipoConsulta);
        planosDeSaude = (Spinner) findViewById(R.id.idPlanosDeSaude);
        especializacao = (Spinner) findViewById(R.id.idSpinnerEspecialazacao);
        spinnerEstado = (Spinner) findViewById(R.id.idSpinnerEstado);
        pesquisar = (AppCompatButton) findViewById(R.id.botaoPesquisar);
        planosDeSaude.setVisibility(View.GONE);
        outroEndereco.setVisibility(View.GONE);
        proximoMim.setVisibility(View.GONE);
    }

    private void setEventos() {
        final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup2.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isChecked = ((RadioButton) group.findViewById(checkedId)).isChecked();
                if (isChecked && checkedId == R.id.idRadioProx) {
                    outroEndereco.setVisibility(View.GONE);
                    proximoMim.setVisibility(View.VISIBLE);
                    radioGroup2.setVisibility(View.VISIBLE);

                }
                if (isChecked && checkedId == R.id.idRadioOutro) {
                    outroEndereco.setVisibility(View.VISIBLE);
                    proximoMim.setVisibility(View.GONE);
                    radioGroup2.setVisibility(View.GONE);
                }
            }
        });
        pesquisar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String planoSaude;
                double[] enderecoPaciente ={0,0};
                if (!tipoConsulta.getSelectedItem().toString().equals("Convênio")) {
                    planoSaude = "null";
                } else {
                    planoSaude = planosDeSaude.getSelectedItem().toString();
                }
                RadioButton r1 = (RadioButton) findViewById(R.id.idRadioProx);
                RadioButton r2 = (RadioButton) findViewById(R.id.idRadioOutro);
                RadioButton r3 = (RadioButton) findViewById(R.id.idRadioMeuEnd);
                RadioButton r4 = (RadioButton) findViewById(R.id.idRadioAtual);
                int distancia = 0;
                Endereco e1 = null;
                if (r1.isChecked()) {
                    if(r3.isChecked()){
                        if(PacienteController.getInstance().getPacienteLogado().getEndereco()==null){
                            DialogAux.dialogOkSimples(activity, activity.getString(R.string.erro),activity.getString(R.string.endeNotCad));
                            return;
                        }
                        enderecoPaciente = Utilitarios.getLatitudeLongitude(activity,PacienteController.getInstance().getPacienteLogado().getEndereco());
                        try {
                            distancia = Integer.parseInt(distanciaKm.getText().toString());
                        }catch (Exception e){
                            DialogAux.dialogOkSimples(activity, activity.getString(R.string.erro), activity.getString(R.string.distMin));
return;
                        }
                    }

                    else if(r4.isChecked()){

                        LocationManager lm = (LocationManager)activity.getSystemService(LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        try {
                            enderecoPaciente[0]  =  location.getLatitude();
                            enderecoPaciente[1]  =  location.getLongitude();
                            try {
                                distancia = Integer.parseInt(distanciaKm.getText().toString());
                            }catch (NumberFormatException e){
                                DialogAux.dialogOkSimples(activity, activity.getString(R.string.erro), activity.getString(R.string.distMin));
                                return;
                            }
                        }catch (Exception e){
                            DialogAux.dialogOkSimples(activity, activity.getString(R.string.erro), activity.getString(R.string.erroLocalAtual));

                        }

                    }
                    else{
                        distancia = 0;
                        e1 = null;
                        enderecoPaciente = null;
                    }

                } else if (r2.isChecked()) {
                    e1 = new Endereco("Brasil", spinnerEstado.getSelectedItem().toString(), cidadeDentista.getText().toString()
                            , bairroDentista.getText().toString(), ruaDentista.getText().toString()
                            , complementoDentista.getText().toString(), 0, 0);
                } else {
                    distancia = 0;
                    e1 = null;
                    enderecoPaciente = null;

                }
                DialogAux.dialogCarregandoSimples(activity);
                PacienteController.getInstance().getDentistasFiltro(activity, nomeDentistaBusca.getText().toString(),
                        tipoConsulta.getSelectedItem().toString(), planoSaude,
                        especializacao.getSelectedItem().toString(), e1, distancia,enderecoPaciente);
            }
        });
        tipoConsulta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (tipoConsulta.getSelectedItem().toString().equals("Convênio")) {
                    planosDeSaude.setVisibility(View.VISIBLE);
                } else
                    planosDeSaude.setVisibility(View.GONE);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onResume() {
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

    @Override
    public String toString() {
        return "blah";
    }


}
