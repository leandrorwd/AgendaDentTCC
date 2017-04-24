package tcc.agendadent.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.AgendaBC;
import tcc.agendadent.gui.dentista.DentistaAgendaDiaria;
import tcc.agendadent.gui.layout_auxiliares.TemplateConsultaAgenda;
import tcc.agendadent.gui.layout_auxiliares.TemplatePacienteConsultasAgendadas;
import tcc.agendadent.gui.layout_auxiliares.TemplateVisualizaMarcacaoConsultaPaciente;
import tcc.agendadent.objetos.AgendaSub;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Work on 18/03/2017.
 */

public class AgendaController {
    private static AgendaController INSTANCE;
    private AgendaBC agendaBC;

    private AgendaController() {
        agendaBC = new AgendaBC();
    }

    public static AgendaController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AgendaController();
        }
        return INSTANCE;
    }


    public void insertSubAgenda(AgendaSub ag) {
        agendaBC.insertAgendaSub(ag, DentistaController.getInstance().getDentistaLogado());
    }

    public void insertConsulta(Activity activity,Consulta consulta, String idDentista, String semestreAno) {
        agendaBC.insertConsulta(activity,consulta, idDentista, semestreAno);
    }

    public void getConsultasSemestre(long idDentista, String anoSemestre, Activity tela, boolean banco) {
        if (!banco) {
            agendaBC.getConsultaSemestre(idDentista, anoSemestre, tela);
        }
    }

    public void getConsultasCompleto(long idDentista, String anoSemestre, Activity tela, boolean banco) {
        if (!banco) {
            agendaBC.getConsultaSemestreCompleto(idDentista, anoSemestre, tela);
        }
    }

    public void setAgendaDiaria(Activity tela, ArrayList<Consulta> consultas) {
        Collections.sort(DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios());
        ArrayList<Horario> horarios = new ArrayList<>();
        int indexHoje = DateTime.now().dayOfWeek().get() - 1;
        for (Horario h : DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios()) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios.add(h);
            }
        }
        ArrayList<Consulta> consultasHoje = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(DateTime.now().dayOfYear())
                    && c.getDataFormat().getYear() == DateTime.now().getYear())) {
                consultasHoje.add(c);
            }
        }

        populaAgendaDiaria(tela, horarios, consultasHoje, R.id.consultasDiarias);
        populaAgendaLateral(tela, AgendaController.getInstance().getConsultasSemestre());

    }

    private void populaAgendaLateral(Activity tela, ArrayList<Consulta> consultas) {
        DateTime data;
        data = DateTime.now().minusDays(1);
        DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios();
        ArrayList<Horario> horarios = new ArrayList<>();
        int indexHoje = data.dayOfWeek().get() - 1;
        for (Horario h : DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios()) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios.add(h);
            }
        }
        ArrayList<Consulta> consultasAmanha = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(data.dayOfYear())
                    && c.getDataFormat().getYear() == data.getYear())) {
                consultasAmanha.add(c);
            }
        }
        populaAgendaDiaria(tela, horarios, consultasAmanha, R.id.consultasDiarias2);

        DateTime data1;
        data1 = DateTime.now().plusDays(1);
        DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios();
        ArrayList<Horario> horarios2 = new ArrayList<>();
        indexHoje = data1.dayOfWeek().get() - 1;
        for (Horario h : DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios()) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios2.add(h);
            }
        }
        ArrayList<Consulta> consultasOntem = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(data1.dayOfYear())
                    && c.getDataFormat().getYear() == data1.getYear())) {
                consultasOntem.add(c);
            }
        }
        populaAgendaDiaria(tela, horarios2, consultasOntem, R.id.consultasDiarias3);
    }

    public void slideDiaria(final Activity tela, final ArrayList<Consulta> consultas, final DateTime newHoje) {
        int anterior = 0;
        int posterior = 0;
        DentistaAgendaDiaria.setTextoData(newHoje);
        if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias) {
            anterior = R.id.consultasDiarias2;
            posterior = R.id.consultasDiarias3;
        } else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias2) {
            anterior = R.id.consultasDiarias3;
            posterior = R.id.consultasDiarias;
        } else if (DentistaAgendaDiaria.idLayout == R.id.consultasDiarias3) {
            anterior = R.id.consultasDiarias;
            posterior = R.id.consultasDiarias2;
        }
        Timer timer;
        timer = new Timer();
        final int finalPosterior = posterior;
        final int finalAnterior = anterior;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tela.runOnUiThread(new Runnable() {
                    public void run() {
                        DateTime data;
                        data = newHoje.minusDays(1);
                        DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios();
                        ArrayList<Horario> horarios = new ArrayList<>();
                        int indexHoje = data.dayOfWeek().get() - 1;
                        for (Horario h : DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios()) {
                            if (h.getDiasSemana().get(indexHoje)) {
                                horarios.add(h);
                            }
                        }
                        ArrayList<Consulta> consultasOntem = new ArrayList<>();
                        for (Consulta c : consultas) {
                            if ((c.getDataFormat().dayOfYear().equals(data.dayOfYear())
                                    && c.getDataFormat().getYear() == data.getYear())) {
                                consultasOntem.add(c);
                            }
                        }
                        populaAgendaDiaria(tela, horarios, consultasOntem, finalAnterior);

                        DateTime data1;
                        data1 = newHoje.plusDays(1);
                        DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios();
                        ArrayList<Horario> horarios2 = new ArrayList<>();
                        indexHoje = data1.dayOfWeek().get() - 1;
                        for (Horario h : DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios()) {
                            if (h.getDiasSemana().get(indexHoje)) {
                                horarios2.add(h);
                            }
                        }
                        ArrayList<Consulta> consultasAmanha = new ArrayList<>();
                        for (Consulta c : consultas) {
                            if ((c.getDataFormat().dayOfYear().equals(data1.dayOfYear())
                                    && c.getDataFormat().getYear() == data1.getYear())) {
                                consultasAmanha.add(c);
                            }
                        }
                        populaAgendaDiaria(tela, horarios2, consultasAmanha, finalPosterior);
                    }
                });
            }
        }, 600, Integer.MAX_VALUE);


    }

    private void populaAgendaDiaria(Activity tela, ArrayList<Horario> horarios, ArrayList<Consulta> consultasMarcadas, int id) {
        LinearLayout horarioDiario = (LinearLayout) tela.findViewById(id);
        if (horarioDiario == null)
            return;
        horarioDiario.removeAllViews();
        if (horarios.isEmpty()) {
            TextView text = new TextView(tela);
            text.setText("Dia não disponivel para consultas");
            horarioDiario.addView(text);
        }
        for (Horario horario : horarios) {
            String horaInicial = horario.getHoraInicial();
            ArrayList<String> horariosAgenda = new ArrayList<>();
            int horaMinuto = 1;
            String[] hFinal = horario.getHoraFinal().split(":");
            String[] parts = horario.getDuracao().split(":");
            int horaFinal = 60 * (Integer.parseInt(hFinal[0]) + Integer.parseInt(hFinal[1]));
            horariosAgenda.add(horaInicial);

            while (horaMinuto < horaFinal - Integer.parseInt(parts[1])) {
                String[] parts2 = horaInicial.split(":");
                int novaHora = (Integer.parseInt(parts[0]) + Integer.parseInt(parts2[0]));
                int novoMinuto = Integer.parseInt(parts[1]) + Integer.parseInt(parts2[1]);

                if (novoMinuto >= 60) {
                    novoMinuto = novoMinuto - 60;
                    novaHora = novaHora + 1;
                }
                horaMinuto = novaHora * 60 + novoMinuto;
                String snovaHora = novaHora + "";
                String snovoMinuto = novoMinuto + "";
                if (novaHora < 10) {
                    snovaHora = "0" + novaHora;
                }
                if (novoMinuto < 10) {
                    snovoMinuto = "0" + novoMinuto;
                }
                horaInicial = snovaHora + ":" + snovoMinuto;
                horariosAgenda.add(horaInicial);
            }
            DateTimeFormatter dateTimeFormatHora = DateTimeFormat.forPattern("HH:mm");
            for (String s : horariosAgenda) {
                boolean entrou = false;
                Consulta aux = null;
                for (Consulta c1 : consultasMarcadas) {

                    DateTime data = new DateTime(c1.getDataConsulta());
                    DateTime dataAux = data.withZone(DateTimeZone.UTC);
                    LocalDateTime dataSemFuso = dataAux.toLocalDateTime();
                    String auxAux = dateTimeFormatHora.print(dataAux);
                    if (horariosAgenda.contains(auxAux)) {
                        if (dateTimeFormatHora.print(dataSemFuso).equals(s)) {
                            TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela, c1, "dentista");
                            horarioDiario.addView(t1);
                            entrou = true;
                            aux = c1;
                            break;
                        }
                    }
                }
                if (!entrou) {
                    TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela, s, "dentista");
                    horarioDiario.addView(t1);
                } else {
                    consultasMarcadas.remove(aux);
                }
            }
            horariosAgenda.clear();
        }

        DialogAux.dialogCarregandoSimplesDismiss();

    }

    private void populaAgendaDiariaMarcacao(Activity tela, ArrayList<Horario> horarios, ArrayList<Consulta> consultasMarcadas, int id) {
        LinearLayout horarioDiario = (LinearLayout) tela.findViewById(id);
        if (horarioDiario == null)
            return;
        horarioDiario.removeAllViews();
        if (horarios.isEmpty()) {
            TextView text = new TextView(tela);
            text.setText("Dia não disponivel para consultas");
            horarioDiario.addView(text);
        }
        for (Horario horario : horarios) {
            String horaInicial = horario.getHoraInicial();
            ArrayList<String> horariosAgenda = new ArrayList<>();
            int horaMinuto = 1;
            String[] hFinal = horario.getHoraFinal().split(":");
            String[] parts = horario.getDuracao().split(":");
            int horaFinal = 60 * (Integer.parseInt(hFinal[0]) + Integer.parseInt(hFinal[1]));
            horariosAgenda.add(horaInicial);

            while (horaMinuto < horaFinal - Integer.parseInt(parts[1])) {
                String[] parts2 = horaInicial.split(":");
                int novaHora = (Integer.parseInt(parts[0]) + Integer.parseInt(parts2[0]));
                int novoMinuto = Integer.parseInt(parts[1]) + Integer.parseInt(parts2[1]);

                if (novoMinuto >= 60) {
                    novoMinuto = novoMinuto - 60;
                    novaHora = novaHora + 1;
                }
                horaMinuto = novaHora * 60 + novoMinuto;
                String snovaHora = novaHora + "";
                String snovoMinuto = novoMinuto + "";
                if (novaHora < 10) {
                    snovaHora = "0" + novaHora;
                }
                if (novoMinuto < 10) {
                    snovoMinuto = "0" + novoMinuto;
                }
                horaInicial = snovaHora + ":" + snovoMinuto;
                horariosAgenda.add(horaInicial);
            }
            DateTimeFormatter dateTimeFormatHora = DateTimeFormat.forPattern("HH:mm");
            for (String s : horariosAgenda) {
                boolean entrou = false;
                Consulta aux = null;
                for (Consulta c1 : consultasMarcadas) {

                    DateTime data = new DateTime(c1.getDataConsulta());
                    DateTime dataAux = data.withZone(DateTimeZone.UTC);
                    LocalDateTime dataSemFuso = dataAux.toLocalDateTime();
                    String auxAux = dateTimeFormatHora.print(dataAux);
                    if (horariosAgenda.contains(auxAux)) {
                        if (dateTimeFormatHora.print(dataSemFuso).equals(s)) {
                            TemplateVisualizaMarcacaoConsultaPaciente t1 = new TemplateVisualizaMarcacaoConsultaPaciente(tela, c1, "paciente", horario.getDuracao());
                            horarioDiario.addView(t1);
                            entrou = true;
                            aux = c1;
                            break;
                        }
                    }
                }
                if (!entrou) {
                    //ADICIONA HORARIO
                    TemplateVisualizaMarcacaoConsultaPaciente t1 = new TemplateVisualizaMarcacaoConsultaPaciente
                            (tela, s, "dentista", horario.getDuracao(), horario);
                    horarioDiario.addView(t1);
                } else {
                    consultasMarcadas.remove(aux);
                }
            }
            horariosAgenda.clear();
        }

        DialogAux.dialogCarregandoSimplesDismiss();

    }


    public ArrayList<Consulta> getConsultasSemestre() {
        return consultasSemestre;
    }

    public void setConsultasSemestre(ArrayList<Consulta> consultasSemestre) {
        this.consultasSemestre = consultasSemestre;
    }

    private ArrayList<Consulta> consultasSemestre;

    public void setAgendaSemestreAtual(ArrayList<Consulta> consultas) {
        consultasSemestre = consultas;
    }

    public void setAgendaCompleta(Activity tela, ArrayList<Consulta> consultas) {
        Collections.sort(DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios());
        ArrayList<Horario> horarios = new ArrayList<>();
        int indexHoje = DateTime.now().dayOfWeek().get() - 1;
        for (Horario h : DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios()) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios.add(h);
            }
        }
        ArrayList<Consulta> consultasHoje = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(DateTime.now().dayOfYear())
                    && c.getDataFormat().getYear() == DateTime.now().getYear())) {
                consultasHoje.add(c);
            }
        }

        populaAgendaDiaria(tela, horarios, consultasHoje, R.id.layoutConsultas);
        //populaAgendaLateral(tela,AgendaController.getInstance().getConsultasSemestre());
    }

    public void setAgendaMarcacao(Activity tela, ArrayList<Consulta> consultas) {
        Collections.sort(PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getAgenda().getHorarios());
        ArrayList<Horario> horarios = new ArrayList<>();
        int indexHoje = DateTime.now().dayOfWeek().get() - 1;
        for (Horario h : PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getAgenda().getHorarios()) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios.add(h);
            }
        }
        ArrayList<Consulta> consultasHoje = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(DateTime.now().dayOfYear())
                    && c.getDataFormat().getYear() == DateTime.now().getYear())) {
                consultasHoje.add(c);
            }
        }

        populaAgendaDiariaMarcacao(tela, horarios, consultasHoje, R.id.layoutConsultas);
    }


    public void carregaAgendaData(final Activity tela, final ArrayList<Consulta> consultas, final DateTime dataParam, final int layout) {
        View myView = tela.findViewById(layout);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(myView, "alpha", 1f, 0);
        fadeOut.setDuration(500);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", 0, 1f);
        fadeIn.setDuration(1000);
        DateTime data;
        data = dataParam;
        ArrayList<Horario> horariosAux = new ArrayList<>();
        if (tela.getLocalClassName().equals("gui.paciente.PacienteVisualizaHorariosMarcacao")) {
            horariosAux = PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getAgenda().getHorarios();
        } else {
            horariosAux = DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios();
        }
        final ArrayList<Horario> horarios = new ArrayList<>();
        int indexHoje = data.dayOfWeek().get() - 1;
        for (Horario h : horariosAux) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios.add(h);
            }
        }
        final ArrayList<Consulta> consultasData = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(data.dayOfYear())
                    && c.getDataFormat().getYear() == data.getYear())) {
                consultasData.add(c);
            }
        }

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        mAnimationSet.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tela.runOnUiThread(new Runnable() {
                    public void run() {
                        populaAgendaDiaria(tela, horarios, consultasData, layout);
                    }
                });
            }
        }, 400, Integer.MAX_VALUE);

    }

    public void carregaAgendaDataAgenda(final Activity tela, final ArrayList<Consulta> consultas, final DateTime dataParam, final int layout) {
        View myView = tela.findViewById(layout);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(myView, "alpha", 1f, 0);
        fadeOut.setDuration(500);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", 0, 1f);
        fadeIn.setDuration(1000);
        DateTime data;
        data = dataParam;
        ArrayList<Horario> horariosAux = new ArrayList<>();
        if (tela.getLocalClassName().equals("gui.paciente.PacienteVisualizaHorariosMarcacao")) {
            horariosAux = PacienteController.getInstance().getUsuarioDentistaMarcaConsulta().getAgenda().getHorarios();
        } else {
            horariosAux = DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios();
        }
        final ArrayList<Horario> horarios = new ArrayList<>();
        int indexHoje = data.dayOfWeek().get() - 1;
        for (Horario h : horariosAux) {
            if (h.getDiasSemana().get(indexHoje)) {
                horarios.add(h);
            }
        }
        final ArrayList<Consulta> consultasData = new ArrayList<>();
        for (Consulta c : consultas) {
            if ((c.getDataFormat().dayOfYear().equals(data.dayOfYear())
                    && c.getDataFormat().getYear() == data.getYear())) {
                consultasData.add(c);
            }
        }

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        mAnimationSet.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tela.runOnUiThread(new Runnable() {
                    public void run() {
                        populaAgendaDiariaMarcacao(tela, horarios, consultasData, layout);
                    }
                });
            }
        }, 400, Integer.MAX_VALUE);

    }

    private DateTime momento;

    public DateTime getMomento() {
        return momento;
    }

    public void setMomento(int ano, int mes, int dia, int hora, int minuto) {
        this.momento = new DateTime(ano, mes, dia, hora, minuto);
    }

    public void getConsultasAgendadasBC(Activity tela, int id) {
        agendaBC.getConsultasPaciente(tela, id);
    }

    public void getHistoricoConsultas(Activity tela, int id) {
        agendaBC.getHistoricoConsultas(tela, id);
    }

    LinearLayout horarioDiario;
    ArrayList<Consulta> consultasBC;
    Activity tela;

    public void buscaAgendaBCAgendadas(Activity tela, ArrayList<Consulta> consultasBC, int id) {
        horarioDiario = (LinearLayout) tela.findViewById(id);
        this.consultasBC = consultasBC;
        this.tela = tela;
        DentistaController.getInstance().getDentistasBC();
    }

    public void buscaAgendaBCAgendadas(ArrayList<UsuarioDentista> dentistas) {
        boolean existeConsulta = false;
        for (Consulta consulta : consultasBC) {
            if (consulta.getIdPaciente() == PacienteController.getInstance().getPacienteLogado().getIdPaciente() && consulta.getDataConsulta() > DateTime.now().getMillis()) {
                TemplatePacienteConsultasAgendadas popular = new TemplatePacienteConsultasAgendadas(tela, consulta, dentistas);
                existeConsulta = true;
                horarioDiario.addView(popular);
            }
        }
        DialogAux.dialogCarregandoSimplesDismiss();
        if (!existeConsulta) {
            DialogAux.dialogOkSimples(tela, "Informação", "Não há consultas futuras agendadas.");
        }
    }

    public void buscaAgendaBCHistorico(Activity tela, ArrayList<Consulta> consultasBC, int id) {
        horarioDiario = (LinearLayout) tela.findViewById(id);
        this.consultasBC = consultasBC;
        this.tela = tela;
        DentistaController.getInstance().getDentistasBCHistorico();
    }

    public void buscaAgendaBCHistorico(ArrayList<UsuarioDentista> dentistas) {
        boolean existeConsulta = false;
        for (Consulta consulta : consultasBC) {
            if (consulta.getIdPaciente() == PacienteController.getInstance().getPacienteLogado().getIdPaciente() && consulta.getDataConsulta() < DateTime.now().getMillis()) {
                TemplatePacienteConsultasAgendadas popular = new TemplatePacienteConsultasAgendadas(tela, consulta, dentistas);
                existeConsulta = true;
                horarioDiario.addView(popular);
            }
        }
        DialogAux.dialogCarregandoSimplesDismiss();
        if (!existeConsulta) {
            DialogAux.dialogOkSimples(tela, "Informação", "Não há histórico de consultas passadas.");
        }
    }
}












