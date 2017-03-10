package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.DialogFragment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.layout_auxiliares.TimePickerFragment;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.DialogAux;

public class ConfigAgendaAdiciona extends AppCompatActivity {

    FloatingActionButton botaoInicio;
    FloatingActionButton botaoFim;
    FloatingActionButton botaoTempo;
    private Button botaoAdicionar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_agenda_adiciona);
        instanciaArtefatos();
        setEventos();

    }

    private void setEventos() {
        botaoInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DentistaController.getInstance().setTelaAuxiliar(ConfigAgendaAdiciona.this);
                DentistaController.getInstance().setHorarioInicio(true);
                DentistaController.getInstance().setHorarioTermino(false);
                DentistaController.getInstance().setHorarioDuracao(false);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"Inicio do Turno");
            }
        });
        botaoFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DentistaController.getInstance().setTelaAuxiliar(ConfigAgendaAdiciona.this);
                DentistaController.getInstance().setHorarioInicio(false);
                DentistaController.getInstance().setHorarioTermino(true);
                DentistaController.getInstance().setHorarioDuracao(false);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"Inicio do Turno");
            }
        });
        botaoTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DentistaController.getInstance().setTelaAuxiliar(ConfigAgendaAdiciona.this);
                DentistaController.getInstance().setHorarioInicio(false);
                DentistaController.getInstance().setHorarioTermino(false);
                DentistaController.getInstance().setHorarioDuracao(true);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"Inicio do Turno");
            }
        });
        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textoHorarioInicial = (TextView) findViewById(R.id.textoHorario);
                TextView textoHorarioFinal = (TextView) findViewById(R.id.textoHorarioEnd);
                TextView textoHorarioDuracao = (TextView) findViewById(R.id.minutosConsulta);
                CheckBox segunda = (CheckBox) findViewById(R.id.checkboxSegunda);
                CheckBox terca = (CheckBox) findViewById(R.id.checkboxTerca);
                CheckBox quarta = (CheckBox) findViewById(R.id.checkBoxQuarta);
                CheckBox quinta = (CheckBox) findViewById(R.id.checkboxQuinta);
                CheckBox sexta = (CheckBox) findViewById(R.id.checkboxSexta);
                CheckBox sabado = (CheckBox) findViewById(R.id.idSabado);
                CheckBox domingo = (CheckBox) findViewById(R.id.domingo);


                boolean[] statusDias = {domingo.isChecked(),segunda.isChecked(),terca.isChecked(),
                        quarta.isChecked(),quinta.isChecked(),sexta.isChecked(),sabado.isChecked()};
                List<Boolean> lista  = new ArrayList<Boolean>();
                lista = Arrays.asList(domingo.isChecked(),segunda.isChecked(),terca.isChecked(),
                        quarta.isChecked(),quinta.isChecked(),sexta.isChecked(),sabado.isChecked());
                Horario horarioNovo = new Horario (textoHorarioInicial.getText().toString(),
                        textoHorarioFinal.getText().toString(),textoHorarioDuracao.getText().toString(),
                        lista);
                if(DentistaController.getInstance().verificaHorario(horarioNovo)){
                    DentistaController.getInstance().addHorarioAgenda(horarioNovo,ConfigAgendaAdiciona.this);
                }
                else{
                    Activity tela = ConfigAgendaAdiciona.this;
                    DialogAux.dialogOkSimples(tela,tela.getResources().getString(R.string.erro),
                            tela.getResources().getString(R.string.horarioColidiu));
                }
            }
        });
    }

    private void instanciaArtefatos() {

        botaoInicio = (FloatingActionButton) findViewById(R.id.botaoInicio);
        botaoFim = (FloatingActionButton) findViewById(R.id.botaoFim);
        botaoTempo = (FloatingActionButton) findViewById(R.id.botaoTempo);
        botaoAdicionar =  (Button) findViewById(R.id.botaoAddHorario);
    }
}
