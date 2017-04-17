package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.objetos.Convenios;
import tcc.agendadent.objetos.Especializacoes;

/**
 * Created by natha on 16/04/2017.
 */

public class DentistaConvenios extends LinearLayout implements Interface_Dentista{
    private Convenios conv;
    private CheckBox amilDental;
    private CheckBox belloDente;
    private CheckBox bradesco;
    private CheckBox doctorClin;
    private CheckBox interodonto;
    private CheckBox metlife;
    private CheckBox odontoEmpresa;
    private CheckBox sulAmerica;
    private CheckBox uniOdonto;

    private Button botaoSalvar;
    private Activity activity;
    private int id_janela;

    public DentistaConvenios(Activity activity, int id_janela) {
        super(activity);
        this.id_janela = id_janela;
        this.activity = activity;
        View.inflate(activity, R.layout.activity_dentista_convenios, this);
        conv = DentistaController.getInstance().getDentistaLogado().getConvenios();
        instanciaArtefatos();
        carregaDados();
        setEventos();
    }

    private void setEventos() {
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override//
            public void onClick(View v) {
                Convenios c1 = new Convenios(
                        amilDental.isChecked(),  belloDente.isChecked(),  bradesco.isChecked(),
                        doctorClin.isChecked(),  interodonto.isChecked(),  metlife.isChecked(),
                        odontoEmpresa.isChecked(),  sulAmerica.isChecked(),  uniOdonto.isChecked()

                );
                DentistaController.getInstance().getDentistaLogado().setConvenios(c1);
                DentistaController.getInstance().atualizaConvenios(activity);
            }
        });
    }

    private void carregaDados() {
        if(conv !=null){
            amilDental.setChecked(conv.isAmilDental());
            belloDente.setChecked(conv.isBelloDente());
            bradesco.setChecked(conv.isBradesco());
            doctorClin.setChecked(conv.isDoctorClin());
            interodonto.setChecked(conv.isInterodonto());
            metlife.setChecked(conv.isMetlife());
            odontoEmpresa.setChecked(conv.isOdontoEmpresa());
            sulAmerica.setChecked(conv.isSulAmerica());
            uniOdonto.setChecked(conv.isUniOdonto());

        }
    }

    private void instanciaArtefatos() {

        amilDental = (CheckBox) findViewById(R.id.idCheckBoxAmil);
        belloDente = (CheckBox) findViewById(R.id.idCheckBoxBelloDente);
        bradesco = (CheckBox) findViewById(R.id.idCheckBoxBradesco);
        doctorClin = (CheckBox) findViewById(R.id.idCheckBoxDoctorClin);
        interodonto = (CheckBox) findViewById(R.id.idCheckBoxInterodonto);
        metlife = (CheckBox) findViewById(R.id.idCheckBoxMetlife);
        odontoEmpresa = (CheckBox) findViewById(R.id.idCheckBoxOdontoEmpresas);
        sulAmerica = (CheckBox) findViewById(R.id.idCheckBoxSulAmericaOdonto);
        uniOdonto = (CheckBox) findViewById(R.id.idCheckBoxUniodonto);
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
