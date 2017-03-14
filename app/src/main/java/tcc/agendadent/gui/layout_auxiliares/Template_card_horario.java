package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.objetos.Horario;

public class Template_card_horario extends LinearLayout {
    private Activity tela;
    private FloatingActionButton edit;
    private TextView textDias;
    private TextView textHorarios;
    private Horario horario;

    public Template_card_horario(Context context, Activity activity,Horario horario) {
        super(context);
        tela = activity;
        this.horario = horario;
        View.inflate(context, R.layout.activity_template_card_horario, this);
        instanciaArtefatos();
        setEventos();
    }

    private void instanciaArtefatos() {
        edit = (FloatingActionButton) findViewById(R.id.botaoInicio);
        textDias = (TextView) findViewById(R.id.textoDias);
        textHorarios = (TextView) findViewById(R.id.textoHorario);
        textHorarios.setText(horario.getHoraInicial() + " - " + horario.getHoraFinal() + " ("+ horario.getDuracao()+")");
        String dias = "";
        if(horario.getDiasSemana().get(0)){
            if(dias.equals("")){
                dias = tela.getResources().getString(R.string.Domingo);
            }

        }if(horario.getDiasSemana().get(1)){
            if(dias.equals("")){
                dias = dias + tela.getResources().getString(R.string.Segunda);
            }
            else
                dias = dias + ", " + tela.getResources().getString(R.string.Segunda);

        } if(horario.getDiasSemana().get(2)){
            if(dias.equals("")){
                dias = dias + tela.getResources().getString(R.string.Terca);
            }
            else
                dias = dias + ", " + tela.getResources().getString(R.string.Terca);
        } if(horario.getDiasSemana().get(3)){
            if(dias.equals("")){
                dias = dias + tela.getResources().getString(R.string.quarta);
            }
            else
                dias = dias + ", " + tela.getResources().getString(R.string.quarta);
        } if(horario.getDiasSemana().get(4)){
            if(dias.equals("")){
                dias = dias + tela.getResources().getString(R.string.quinta);
            }
            else
                dias = dias + ", " + tela.getResources().getString(R.string.quinta);
        } if(horario.getDiasSemana().get(5)){
            if(dias.equals("")){
                dias = dias + tela.getResources().getString(R.string.sexta);
            }
            else
                dias = dias + ", " + tela.getResources().getString(R.string.sexta);
        } if(horario.getDiasSemana().get(6)){
            if(dias.equals("")){
                dias = dias + tela.getResources().getString(R.string.sabado);
            }
            else
                dias = dias + ", " + tela.getResources().getString(R.string.sabado);
        }
        dias = dias.replace("-Feira","");
        textDias.setText(dias);
    }

    private void setEventos() {

    }
}
