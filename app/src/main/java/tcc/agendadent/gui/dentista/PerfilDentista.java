package tcc.agendadent.gui.dentista;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import tcc.agendadent.R;

public class PerfilDentista extends LinearLayout implements ClassesDentista   {
    private int id_janela;

    public PerfilDentista(Activity activity, int id_janela) {
        super(activity);
        this.id_janela = id_janela;
        View.inflate(activity, R.layout.activity_perfil_dentista, this);

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
}
