package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.gui.paciente.PacienteVisualizaHorariosMarcacao;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.OnSwipeTouchListener;

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


        layout.setOnTouchListener(new OnSwipeTouchListener(activity) {

            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
            }

            public void onClick(MotionEvent event) {
                float x = event.getX() + layout.getLeft();
                float y = event.getY() + layout.getTop();

                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    layout.drawableHotspotChanged(x, y);
                }

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        layout.setPressed(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        layout.setPressed(true);
                        break;
                }
                PacienteController.getInstance().setDentistaMarcaConsulta(dentista);
                activity.startActivity(new Intent(activity, PacienteVisualizaHorariosMarcacao.class));
            }

        });

    }

    private void instanciaArtefatos() {
        fotoPerfil = (ImageView) findViewById(R.id.fotoDentistaImageView);
        nome = (TextView) findViewById(R.id.textViewNome);
        nota = (TextView) findViewById(R.id.nota);
        especialidades = (TextView) findViewById(R.id.especialidades);
        endereco = (TextView) findViewById(R.id.endereco);
        layout = (CardView) findViewById(R.id.card_view);
        nome.setText(dentista.getNomeCompleto());
        nota.setText("5.0");
        especialidades.setText(dentista.getEspecializacoesString());
        endereco.setText(dentista.getEndereco().toString());
        DentistaController.getInstance().setImagemPerfilLoading(activity,dentista.getUrlFotoPerfil(),fotoPerfil);
    }
}
