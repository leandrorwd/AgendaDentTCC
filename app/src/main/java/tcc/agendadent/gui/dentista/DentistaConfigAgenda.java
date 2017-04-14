package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

public class DentistaConfigAgenda extends LinearLayout  implements Interface_Dentista {

    FloatingActionButton botaoAdd;
    FloatingActionButton botaoSave;
    private Activity activity;

    public DentistaConfigAgenda(Activity activity, int id_janela) {
        super(activity);
        this.activity = activity;
        View.inflate(activity, R.layout.dentista_config_agenda_conteudo, this);
        instanciaArtefatos();
        setEventos();
        if(!ValidationTest.verificaInternet(activity)){
            dialogOkSimples(activity,"Erro", DentistaConfigAgenda.this.getResources().getString(R.string.internetSemConexao));
            return;
        }
        carregaHorarios();
        this.id = id_janela;
    }

    private void carregaHorarios() {
        DentistaController.getInstance().carregaHorarios(activity,DentistaController.getInstance().getDentistaLogado(),false);
    }

    private void setEventos() {
        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, DentistaConfigAgendaAdiciona.class));
            }
        });
        botaoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Horario h : DentistaController.getInstance().getHorariosTemporarios()){
                    DentistaController.getInstance().getDentistaLogado().getAgenda().addHorario(h);
                }
                DentistaController.getInstance().setDentista(activity,DentistaController.getInstance().getDentistaLogado(),false);
            }
        });
    }

    private void instanciaArtefatos() {
        botaoSave =(FloatingActionButton) findViewById(R.id.botaoSave);
        botaoAdd = (FloatingActionButton) findViewById(R.id.botaoAdd);
    }


    @Override
    public void onResume() {
        if(!ValidationTest.verificaInternet(activity)){
            dialogOkSimples(activity,"Erro", DentistaConfigAgenda.this.getResources().getString(R.string.internetSemConexao));
            return;
        }
        carregaHorarios();
    }

    @Override
    public boolean needResume() {
        return true;
    }
    private int id;
    @Override
    public int getIdMenu(){
        return id;
    }

    @Override
    public void flipper(boolean next) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
