package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.controllers.AgendaController;
import tcc.agendadent.controllers.DentistaController;
import tcc.agendadent.gui.dentista.DentistaAgendaDiaria;
import tcc.agendadent.gui.dentista.DentistaVisualizarConsulta;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.OnSwipeTouchListener;

public class TemplateConsultaAgenda extends RelativeLayout {

    private Activity tela;
    private Consulta consulta;
    private String usuarioTipo;
    private String horaInicial;
    private boolean isLivre;
    private Horario horario;

    public TemplateConsultaAgenda(Activity tela, Consulta c1, String usuario, Horario horario) {
        super(tela);
        this.tela = tela;
        isLivre = false;
        usuarioTipo = usuario;
        this.horario = horario;
        View.inflate(tela, R.layout.template_consulta_agenda, this);
        setEventos();
        preencheHorario(c1);
        consulta = c1;
        setEventos();
    }


    public TemplateConsultaAgenda(Activity tela, String horaInicial, String usuario, Horario horario) {
        super(tela);
        this.tela = tela;
        this.horario = horario;
        usuarioTipo = usuario;
        View.inflate(tela, R.layout.template_consulta_agenda, this);
        this.horaInicial = horaInicial;
        isLivre = false;
        setEventos();
        horarioLivre(horaInicial);
    }

    public TemplateConsultaAgenda(Activity tela, String horaInicial, String usuario, boolean tempo, Horario horario) {
        super(tela);
        this.tela = tela;
        isLivre = true;
        this.horario = horario;
        usuarioTipo = usuario;
        View.inflate(tela, R.layout.template_consulta_agenda, this);
        this.horaInicial = horaInicial;
        // setEventos();
        horarioLivre(horaInicial, tempo);
    }

    private void horarioLivre(String horaInicial, boolean f) {
        isLivre = true;
        CardView card = (CardView) findViewById(R.id.card_view);
        card.setClickable(false);
        TextView textoAux = (TextView) findViewById(R.id.textNomePaciente);
        textoAux.setText("Tempo insuficiente.");
        textoAux.setPadding(12, 52, 0, 0);
        textoAux = (TextView) findViewById(R.id.textoHora);
        textoAux.setText(horaInicial);
        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setVisibility(View.GONE);

    }

    private void horarioLivre(String horaInicial) {
        isLivre = true;
        TextView textoAux = (TextView) findViewById(R.id.textNomePaciente);
        textoAux.setText("Livre");
        textoAux.setPadding(12, 52, 0, 0);
        textoAux = (TextView) findViewById(R.id.textoHora);
        textoAux.setText(horaInicial);
        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setVisibility(View.GONE);

    }

    //okok
    private void preencheHorario(Consulta c) {
        TextView textoAux = (TextView) findViewById(R.id.textoHora);
        DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm");

        DateTime data = new DateTime(c.getDataConsulta());
        DateTime dataAux = data.withZone(DateTimeZone.UTC);
        LocalDateTime dataSemFuso = dataAux.toLocalDateTime();

        textoAux.setText(format.print(dataSemFuso));

        textoAux = (TextView) findViewById(R.id.textNomePaciente);
        textoAux.setText(c.getNomePaciente());

        textoAux = (TextView) findViewById(R.id.textTipoConsulta);
        textoAux.setText(c.getTipoConsulta());
    }

    private void setEventos() {
        final CardView card = (CardView) findViewById(R.id.card_view);
        if (card != null) {
            card.setOnTouchListener(new OnSwipeTouchListener(tela) {

                public void onSwipeRight() {
                    DentistaAgendaDiaria.flipperHelper(tela, true);
                }

                public void onSwipeLeft() {
                    DentistaAgendaDiaria.flipperHelper(tela, false);
                }

                public void onClick(MotionEvent event) {
                    float x = event.getX() + card.getLeft();
                    float y = event.getY() + card.getTop();

                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        card.drawableHotspotChanged(x, y);
                    }

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            card.setPressed(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            card.setPressed(true);
                            break;
                    }


                    Intent i = new Intent(tela, DentistaVisualizarConsulta.class);
                    if (usuarioTipo.equals("dentista")) {
                        if (tela.getLocalClassName().equals("gui.dentista.DentistaAgendarConsultaEspecial")) {
                            if (isLivre()) {
                                new AlertDialog.Builder(tela)
                                        .setTitle(tela.getResources().getString(R.string.Salvar))
                                        .setMessage(tela.getResources().getString(R.string.confirmaConsulta))
                                        .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DialogAux.dialogCarregandoSimples(tela);
                                                salvaConsultaEspecial(true, null);
                                            }
                                        }).setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                        .show();
                            }

                        } else {
                            if (consulta != null) {
                                if(consulta.getNomePaciente().contains("Indisponivel")){
                                    new AlertDialog.Builder(tela)
                                            .setTitle(tela.getResources().getString(R.string.Aviso))
                                            .setMessage(tela.getResources().getString(R.string.reabilitarConsulta))
                                            .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                   AgendaController.getInstance().reativaConsulta(tela,consulta);
                                                }
                                            }).setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                            .show();
                                }
                                else{
                                    i.putExtra("consulta", consulta);
                                    i.putExtra("user", usuarioTipo);
                                    tela.startActivity(i);
                                }
                            } else {
                                if (AgendaController.getInstance().getDataAux().getYear() <= DateTime.now().getYear()) {
                                    if (AgendaController.getInstance().getDataAux().getDayOfYear() <= DateTime.now().getDayOfYear()) {
                                        if (AgendaController.getInstance().getDataAux().getDayOfYear() == DateTime.now().getDayOfYear()) {
                                            if (Integer.parseInt(horaInicial.substring(0, 2)) <= DateTime.now().getHourOfDay()) {
                                                DialogAux.dialogOkSimples(tela, tela.getString(R.string.erro), tela.getString(R.string.erroSuspender));
                                                return;
                                            } else {
                                                DialogAux.dialogOkSimples(tela, tela.getString(R.string.erro), tela.getString(R.string.erroMarcarConsultaPassado));
                                                return;
                                            }
                                        } else {
                                            DialogAux.dialogOkSimples(tela, tela.getString(R.string.erro), tela.getString(R.string.erroSuspender));
                                            return;
                                        }
                                    }
                                    else{
                                        dialogSuspender();
                                    }
                                } else {
                                    dialogSuspender();
                                }
                            }
                        }
                    } else {
                        if (consulta.getDataFormat().minusHours(1).isAfter(DateTime.now())) {
                            DialogAux.dialogOkSimples(tela, "Erro", "Não é possivel realizar uma operação neste horario.");
                            return;
                        }
                        i.putExtra("consulta", consulta);
                        i.putExtra("user", usuarioTipo);
                        tela.startActivity(i);
                    }

                }

            });
        }
    }

    public void salvaConsultaEspecial(boolean principal, Consulta consultaMestre) {

        int slotConsultas = AgendaController.getInstance().getNumeroHorarios() + 1;
        ArrayList<TemplateConsultaAgenda> lista = AgendaController.getInstance().getListaDia();
        long duracaoConsultaHorario = 0;
        String tipoConsultaString = "";
        if (AgendaController.getInstance().getTipoConsultaEspecial().equals("Convênio")) {
            tipoConsultaString = AgendaController.getInstance().getTipoConsultaEspecial() + " - "
                    + (AgendaController.getInstance().getPlanoSaudeEspecial());
        } else {
            tipoConsultaString = AgendaController.getInstance().getTipoConsultaEspecial();
        }

        String[] parts = horaInicial.split(":");
        DateTime dateDataConsultaInicial = new DateTime(AgendaController.getInstance().getMomento().getYear(),
                AgendaController.getInstance().getMomento().getMonthOfYear(),
                AgendaController.getInstance().getMomento().getDayOfMonth(),
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]), 0, 0);

        String[] parts2 = horario.getDuracao().split(":");

        long valor = ((Integer.parseInt(parts2[0]) * 60) + Integer.parseInt(parts2[1])) * 60000;
        duracaoConsultaHorario = valor;
        int mes = DateTime.now().monthOfYear().get();
        String anoSemestre;
        if (mes >= 7) {
            anoSemestre = DateTime.now().year().get() + "A2";
        } else
            anoSemestre = DateTime.now().year().get() + "A1";
        anoSemestre = anoSemestre.replace("A", "");
        long valorAux = 0;
        try {
            String operador = DateTime.now().toString().substring(23, 26);
            valorAux = Integer.parseInt(DateTime.now().toString().substring(24, 26));
            if (operador.contains("+")) {
                valorAux = valorAux * 3600000;
            } else {
                valorAux = (valorAux * 3600000) - (valorAux * 3600000) * 2;
            }
        } catch (Exception e) {
            valorAux = 0;
        }

        if (principal) {
            String s = dateDataConsultaInicial.getMillis() + "";
            String s2 = valorAux + "";
            Consulta c1 = new Consulta(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                    , AgendaController.getInstance().getUsuarioPacienteConsultaEspecial().getIdPaciente(),
                    dateDataConsultaInicial.getMillis() + valorAux
                    , 5, tipoConsultaString, AgendaController.getInstance().getUsuarioPacienteConsultaEspecial().getNome(), duracaoConsultaHorario * (slotConsultas - 1), false);
            if (1 < slotConsultas) {
                c1.setConsultaMultiplaPai(true);
            }
            AgendaController.getInstance().insertConsulta(tela, c1,
                    DentistaController.getInstance().getDentistaLogado().getIdDentista() + "", anoSemestre);


            DialogAux.dialogCarregandoSimplesDismiss();
            for (int i = 1; i < slotConsultas; i++) {
                lista.get(indexTela).salvaConsultaEspecial(false, c1);
                indexTela++;
            }

            DialogAux.dialogCarregandoSimplesDismiss();
            tela.finish();
        } else {
            Consulta c1 = new Consulta(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                    , AgendaController.getInstance().getUsuarioPacienteConsultaEspecial().getIdPaciente(),
                    dateDataConsultaInicial.getMillis() + valorAux
                    , 5, tipoConsultaString, AgendaController.getInstance().getUsuarioPacienteConsultaEspecial().getNome(), valor, false);
            c1.setConsultaMultipla(true);
            c1.setDataConsultaPrimaria(consultaMestre.getDataConsulta());
            AgendaController.getInstance().insertConsultaSecundaria(tela, c1,
                    DentistaController.getInstance().getDentistaLogado().getIdDentista() + "", anoSemestre, consultaMestre);

        }
    }

    private void dialogSuspender() {
        new AlertDialog.Builder(tela)
                .setTitle(tela.getResources().getString(R.string.NaoHaConsultaMarcada))
                .setMessage(tela.getResources().getString(R.string.ocuparConsulta))
                .setPositiveButton(tela.getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DateTime aux = DateTime.now();
                        int hora = Integer.parseInt(horaInicial.substring(0, 2));
                        int min = Integer.parseInt(horaInicial.substring(3, 5));
                        try {
                            String operador = aux.toString().substring(23, 26);
                            int valor = Integer.parseInt(aux.toString().substring(24, 26));
                            if (operador.contains("+")) {
                                hora = hora + valor;
                            } else {
                                hora = hora - valor;
                            }
                        } catch (Exception e) {

                        }
                        DateTime date0 = new DateTime(AgendaController.getInstance().getMomento().getYear(),
                                AgendaController.getInstance().getMomento().getMonthOfYear(),
                                AgendaController.getInstance().getMomento().getDayOfMonth(), hora, min);
                        Consulta c1 = new Consulta(DentistaController.getInstance().getDentistaLogado().getIdDentista()
                                , 0, date0.getMillis(), 0, "-", "Indisponivel", 0, false);
                        String anoSemestre;
                        if (date0.getMonthOfYear() >= 7) {
                            anoSemestre = DateTime.now().year().get() + "A2";
                        } else
                            anoSemestre = DateTime.now().year().get() + "A1";
                        anoSemestre = anoSemestre.replace("A", "");
                        AgendaController.getInstance().insertConsulta(tela, c1, DentistaController.getInstance().getDentistaLogado().getIdDentista() + ""
                                , anoSemestre);
                        DentistaController.getInstance().callResume();

                    }
                })
                .setNegativeButton(tela.getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public boolean isLivre() {
        return isLivre;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    private int indexTela;

    public void setIndexTela(int indexTela) {
        this.indexTela = indexTela;
    }
}
