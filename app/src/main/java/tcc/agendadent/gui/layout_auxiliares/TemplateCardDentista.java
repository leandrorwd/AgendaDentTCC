package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.R;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import tcc.agendadent.objetos.Consulta;

/**
 * Created by natha on 18/04/2017.
 */

public class TemplateCardDentista extends RelativeLayout {

    private Activity activity;
    private UsuarioDentista dentista;
    private CardView layout;
    private TextView nome;
    private TextView nota;
    private TextView especialidades;
    private TextView endereco;
    private ImageView fotoPerfil;



    public TemplateCardDentista(Activity tela, UsuarioDentista user) {
        super(tela);
        this.activity=tela;
        dentista = user;
        View.inflate(tela, R.layout.template_card_dentista, this);
        instanciaArtefatos();
        setEventos();
    }

    private void setEventos() {

    }

    private void instanciaArtefatos() {
        fotoPerfil = (ImageView) findViewById(R.id.fotoDentistaImageView);
        nome = (TextView) findViewById(R.id.textViewNome);
        nota = (TextView) findViewById(R.id.nota);
        especialidades = (TextView) findViewById(R.id.especialidades);
        endereco = (TextView) findViewById(R.id.endereco);
        nome.setText(dentista.getNomeCompleto());
        nota.setText("5.0");
        especialidades.setText(dentista.getEspecializacoesString());
        endereco.setText(dentista.getEndereco().toString());
        DentistaController.getInstance().setImagemPerfilLoading(activity,dentista.getUrlFotoPerfil(),fotoPerfil);
    }
}
