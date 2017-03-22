package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.layout_auxiliares.Template_card_horario;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

public class ConfigAgenda extends LinearLayout   {

    FloatingActionButton botaoAdd;
    FloatingActionButton botaoSave;
    private Activity activity;

    public ConfigAgenda(Activity activity) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.activity_config_agenda_conteudo, this);
        instanciaArtefatos();
        setEventos();
        if(!ValidationTest.verificaInternet(activity)){
            dialogOkSimples(activity,"Erro",ConfigAgenda.this.getResources().getString(R.string.internetSemConexao));
            return;
        }
        carregaHorarios();
    }

    private void carregaHorarios() {
        DentistaController.getInstance().carregaHorarios(activity,DentistaController.getInstance().getDentistaLogado(),false);
    }

    private void setEventos() {
        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ConfigAgendaAdiciona.class));
            }
        });
        botaoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DentistaController.getInstance().setDentista(activity,DentistaController.getInstance().getDentistaLogado(),false);
            }
        });
    }

    private void instanciaArtefatos() {
        botaoSave =(FloatingActionButton) findViewById(R.id.botaoSave);
        botaoAdd = (FloatingActionButton) findViewById(R.id.botaoAdd);
    }


}
