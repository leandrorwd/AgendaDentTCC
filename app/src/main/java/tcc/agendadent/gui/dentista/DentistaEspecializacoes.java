package tcc.agendadent.gui.dentista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.objetos.Especializacoes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Button;

public class DentistaEspecializacoes extends LinearLayout implements Interface_Dentista {
    private Especializacoes espec;
    private CheckBox clinicoGeral;
    private CheckBox endodontia;
    private CheckBox implantodontia;
    private CheckBox ortodontia;
    private CheckBox odontopedia;
    private CheckBox odontologiaEstetica;
    private CheckBox protese;
    private CheckBox sus;

    private Button botaoSalvar;
    private Activity activity;
    private int id_janela;

    public DentistaEspecializacoes(Activity activity, int id_janela) {
        super(activity);
        this.id_janela = id_janela;
        this.activity = activity;
        View.inflate(activity, R.layout.activity_dentista_especializacoes, this);
        espec = DentistaController.getInstance().getDentistaLogado().getEspecializacoes();
        instanciaArtefatos();
        carregaDados();
        setEventos();
    }

    private void setEventos() {
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//boolean clinicoGeral, boolean endodontia, boolean ortodontia, boolean odontopediatria, boolean odontologiaEstetica, boolean protese)
                Especializacoes e1 = new Especializacoes(
                        clinicoGeral.isChecked(), endodontia.isChecked(),
                        ortodontia.isChecked(),odontopedia.isChecked(),
                        odontologiaEstetica.isChecked(),protese.isChecked(),
                        implantodontia.isChecked()
                );
                DentistaController.getInstance().getDentistaLogado().setEspecializacoes(e1);
                DentistaController.getInstance().atualizaEspecilizacao(activity);
            }
        });
    }

    private void carregaDados() {
        if(espec !=null){
            clinicoGeral.setChecked(espec.isClinicoGeral());
            endodontia .setChecked(espec.isEndodontia());
            implantodontia .setChecked(espec.isImplantodontia());
            ortodontia .setChecked(espec.isOrtodontia());
            odontopedia .setChecked(espec.isOdontopediatria());
            odontologiaEstetica .setChecked(espec.isOdontologiaEstetica());
            protese .setChecked(espec.isProtese());
        }
    }

    private void instanciaArtefatos() {
        clinicoGeral = (CheckBox) findViewById(R.id.idCheckBoxClinicoGeral);
        endodontia = (CheckBox) findViewById(R.id.idCheckBoxEndodontia);
        implantodontia = (CheckBox) findViewById(R.id.idCheckBoxImplantodontia);
        ortodontia = (CheckBox) findViewById(R.id.idCheckBoxOrtodontia);
        odontopedia = (CheckBox) findViewById(R.id.idCheckBoxOdontopediatria);
        odontologiaEstetica = (CheckBox) findViewById(R.id.idCheckBoxEstetica);
        protese = (CheckBox) findViewById(R.id.idCheckBoxProtese);
        botaoSalvar =(Button) findViewById(R.id.botaoSalvar);
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
        return id_janela;
    }

    @Override
    public void flipper(boolean next) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
