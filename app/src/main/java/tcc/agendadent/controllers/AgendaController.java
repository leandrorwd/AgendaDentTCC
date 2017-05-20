package tcc.agendadent.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import tcc.agendadent.bancoConnection.PacienteBC;
import tcc.agendadent.gui.dentista.DentistaAgendaDiaria;
import tcc.agendadent.gui.dentista.DentistaAgendarConsultaEspecial;
import tcc.agendadent.gui.layout_auxiliares.TemplateConsultaAgenda;
import tcc.agendadent.gui.layout_auxiliares.TemplatePacienteConsultasAgendadas;
import tcc.agendadent.gui.layout_auxiliares.TemplatePacienteHistoricoConsultas;
import tcc.agendadent.gui.layout_auxiliares.TemplateSemConsultas;
import tcc.agendadent.gui.layout_auxiliares.TemplateVisualizaMarcacaoConsultaPaciente;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.objetos.UsuarioDentista;
import tcc.agendadent.objetos.UsuarioPaciente;
import tcc.agendadent.servicos.DialogAux;
import tcc.agendadent.servicos.ValidationTest;

import static tcc.agendadent.servicos.DialogAux.dialogOkSimples;

/**
 * Created by Work on 18/03/2017.
 */

public class AgendaController {
    private static AgendaController INSTANCE;
    private AgendaBC agendaBC;
    private ArrayList<Consulta> consultasSemestre;
    private DateTime momento;
    private LinearLayout horarioDiario;
    private ArrayList<Consulta> consultasBC;
    private Activity tela;
    private UsuarioPaciente usuarioPaciente;
    private int numeroHorarios;
    private String tipoConsultaEspecial;
    private String planoSaudeEspecial;
    private DateTime auxiliar;
    private ArrayList<TemplateConsultaAgenda> listaDia;


    private AgendaController() {
        agendaBC = new AgendaBC();
    }

    public static AgendaController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AgendaController();
        }
        return INSTANCE;
    }

    public void insertConsulta(Activity activity, Consulta consulta, String idDentista, String semestreAno) {
        agendaBC.insertConsulta(activity, consulta, idDentista, semestreAno);
    }
    public void insertConsultaSecundaria(Activity activity, Consulta consulta, String idDentista, String semestreAno,Consulta consultaMestre) {
        agendaBC.insertConsultaSecundaria(activity, consulta, idDentista, semestreAno,consultaMestre);
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
        int indexHoje = AgendaController.getInstance().getDataAux().getDayOfWeek() - 1;
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
        int i = consultasHoje.size();
        int f = horarios.size();

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
                        if (tela.getLocalClassName().equals("gui.dentista.DentistaAgendarConsultaEspecial")) {
                            populaAgendaDiariaEspecial(tela, horarios, consultasOntem, finalAnterior);
                        } else
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
                        if (tela.getLocalClassName().equals("gui.dentista.DentistaAgendarConsultaEspecial")) {
                            populaAgendaDiariaEspecial(tela, horarios2, consultasAmanha, finalPosterior);
                        } else
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

        if(consultasMarcadas==null ||consultasMarcadas.size()==0|| consultasMarcadas.get(0)==null){}
        else{
            AgendaController.getInstance().setDiaAtual(consultasMarcadas.get(0).getDataFormat().getYear()
                    ,consultasMarcadas.get(0).getDataFormat().getMonthOfYear(),consultasMarcadas.get(0).getDataFormat().getDayOfMonth(),12);
           // DentistaAgendaDiaria.setTextoData( AgendaController.getInstance().getDataAux(
            }

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
                LocalDateTime aux1 = null;
                for (Consulta c1 : consultasMarcadas) {
                    if (!c1.getCancelada()) {
                        DateTime data = new DateTime(c1.getDataConsulta());
                        DateTime dataAux = data.withZone(DateTimeZone.UTC);
                        LocalDateTime dataSemFuso = dataAux.toLocalDateTime();
                        String auxAux = dateTimeFormatHora.print(dataAux);
                        if (horariosAgenda.contains(auxAux)) {
                            if (dateTimeFormatHora.print(dataSemFuso).equals(s)) {
                                TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela, c1, "dentista",horario);
                                horarioDiario.addView(t1);
                                entrou = true;
                                aux = c1;
                                break;
                            }
                        }
                    }
                }
                if (!entrou) {
                    TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela, s, "dentista",horario);
                    horarioDiario.addView(t1);
                } else {
                    consultasMarcadas.remove(aux);
                }
            }
            horariosAgenda.clear();
        }

        DialogAux.dialogCarregandoSimplesDismiss();

    }

    private void populaAgendaDiariaEspecial(Activity tela, ArrayList<Horario> horarios, ArrayList<Consulta> consultasMarcadas, int id) {
        LinearLayout horarioDiario = (LinearLayout) tela.findViewById(id);
        ArrayList<TemplateConsultaAgenda> todosHorarios = new ArrayList<>();
        if (horarioDiario == null)
            return;
        horarioDiario.removeAllViews();
        if (horarios.isEmpty()) {
            TextView text = new TextView(tela);
            text.setText("Dia não disponivel para consultas");
            horarioDiario.addView(text);
        }
        int indice =0;
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
            int controleHorarios = numeroHorarios;
            ArrayList<TemplateConsultaAgenda> listaDia = new ArrayList<>();
            for (String s : horariosAgenda) {
                boolean entrou = false;
                Consulta aux = null;
                LocalDateTime aux1 = null;
                for (Consulta c1 : consultasMarcadas) {
                    if (!c1.getCancelada()) {
                        DateTime data = new DateTime(c1.getDataConsulta());
                        DateTime dataAux = data.withZone(DateTimeZone.UTC);
                        LocalDateTime dataSemFuso = dataAux.toLocalDateTime();
                        String auxAux = dateTimeFormatHora.print(dataAux);
                        if (horariosAgenda.contains(auxAux)) {
                            if (dateTimeFormatHora.print(dataSemFuso).equals(s)) {
                                TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela, c1, "dentista",horario);
                                listaDia.add(t1);
                                //  horarioDiario.addView(t1);
                                entrou = true;
                                aux = c1;
                                break;
                            }
                        }
                    }
                }
                if (!entrou) {
                        TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela, s, "dentista",horario);
                        listaDia.add(t1);
                        //   horarioDiario.addView(t1);
                } else {
                    consultasMarcadas.remove(aux);
                }
            }
            int i = 0;
            int j = numeroHorarios-1 ;
            for (; j <= horariosAgenda.size(); i++, j++) {
                boolean possivel = true;
                for (int n = i; n <= j; n++) {
                    try {
                        if (listaDia.get(n).isLivre()) {
                            continue;
                        } else {
                            possivel = false;
                            break;
                        }
                    }catch (IndexOutOfBoundsException e){

                    }
                }
                if (!possivel) {
                    if (listaDia.get(i).isLivre()) {
                        TemplateConsultaAgenda aux = new TemplateConsultaAgenda(tela, horariosAgenda.get(i), "dentista", true,horario);
                        listaDia.remove(i);
                        listaDia.add(i, aux);
                    }
                }
            }
            //TODO LASTs HORARIO
            for (TemplateConsultaAgenda t1 : listaDia) {
                t1.setIndexTela(indice);
                horarioDiario.addView(t1);
                indice++;
            }
            todosHorarios.addAll(listaDia);
            horariosAgenda.clear();
        }
        setListaConsultasDiarias(todosHorarios);
        DialogAux.dialogCarregandoSimplesDismiss();

    }

    private void setListaConsultasDiarias(ArrayList<TemplateConsultaAgenda> listaDia) {
        this.listaDia = listaDia;
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
        if (tela.getLocalClassName().equals("gui.dentista.DentistaAgendarConsultaEspecial")) {
            populaAgendaDiariaEspecial(tela, horarios, consultasHoje, R.id.layoutConsultas);
        } else
            populaAgendaDiaria(tela, horarios, consultasHoje, R.id.layoutConsultas);
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
                        if (tela.getLocalClassName().equals("gui.dentista.DentistaAgendarConsultaEspecial")) {
                            populaAgendaDiariaEspecial(tela, horarios, consultasData, R.id.layoutConsultas);
                        } else
                            populaAgendaDiaria(tela, horarios, consultasData, R.id.layoutConsultas);
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

    public DateTime getMomento() {
        return momento;
    }

    public void setMomento(int ano, int mes, int dia, int hora, int minuto) {
        this.momento = new DateTime(ano, mes, dia, hora, minuto);
    }

    public void getConsultasAgendadasBC(long idPaciente, Activity tela, LinearLayout layout, boolean consulta) {
        agendaBC.getConsultasPaciente(idPaciente, tela, layout, consulta);
    }

    public void buscaAgendaBCAgendadas(Activity tela, ArrayList<Consulta> consultasBC, LinearLayout horario, boolean consulta) {
        horarioDiario = horario;
        this.consultasBC = consultasBC;
        this.tela = tela;
        DentistaController.getInstance().getDentistasBC(consulta);
    }

    public void buscaAgendaBCAgendadas(ArrayList<UsuarioDentista> dentistas, boolean consultaVerif) {
        boolean existeConsulta = false;
        if (horarioDiario != null)
            horarioDiario.removeAllViews();
        for (Consulta consulta : consultasBC) {
            // próximas consultas
            if ((consultaVerif && consulta.getIdPaciente() == PacienteController.getInstance().getPacienteLogado().getIdPaciente() && consulta.getDataConsulta() > DateTime.now().getMillis()) && !consulta.getCancelada()) {
//                DialogAux.dialogOkSimples(tela, "msg", String.valueOf(consulta.getCancelada()));
                TemplatePacienteConsultasAgendadas popular = new TemplatePacienteConsultasAgendadas(tela, consulta, dentistas);
                existeConsulta = true;
                horarioDiario.addView(popular);
            } else
                // histórico de consultas
                if ((!consultaVerif && consulta.getIdPaciente() == PacienteController.getInstance().getPacienteLogado().getIdPaciente() && consulta.getDataConsulta() < DateTime.now().getMillis()) || (!consultaVerif && consulta.getCancelada())) {
                    TemplatePacienteHistoricoConsultas popular = new TemplatePacienteHistoricoConsultas(tela, consulta, dentistas);
                    existeConsulta = true;
                    horarioDiario.addView(popular);
                }
        }
        DialogAux.dialogCarregandoSimplesDismiss();
        if (!existeConsulta) {
            TemplateSemConsultas semconsulta = new TemplateSemConsultas(tela);
            horarioDiario.addView(semconsulta);
        }
    }

    public void desmarcarConsulta(Activity tela, Consulta consulta) {
        int mes = consulta.getDataFormat().monthOfYear().get();
        String ano = String.valueOf(consulta.getDataFormat().year().get());

        if (mes <= 6) {
            ano += "1";
        } else ano += "2";

        agendaBC.desmarcarConsulta(tela, consulta, ano);
    }

    public void setDiaAtual(int ano, int mes, int dia, int hora) {
        auxiliar = new DateTime(ano, mes, dia, hora, 0);
        //DentistaAgendaDiaria.setTextoData(auxiliar);
    }

    public DateTime getDataAux() {
        return auxiliar;
    }

    public void getPacienteViaMarcacaoConsulta(String email, Activity activity, int posicaoSpinner, String tipoConsulta, String planoSaude) {
        tipoConsultaEspecial = tipoConsulta;
        planoSaudeEspecial = planoSaude;
        agendaBC.getPacienteViaMarcacaoConsulta(email, activity, posicaoSpinner + 1);
    }

    public void navegaAgendaEspecial(Activity activity, UsuarioPaciente usuarioPaciente, int numeroHorarios) {
        this.numeroHorarios = numeroHorarios;
        this.usuarioPaciente = usuarioPaciente;
        DialogAux.dialogCarregandoSimplesDismiss();
        if(usuarioPaciente!=null){
            activity.startActivity(new Intent(activity, DentistaAgendarConsultaEspecial.class));
        }
        else{
            DialogAux.dialogOkSimples(activity,"Erro","Nenhum paciente encontrado.");
        }
    }

    public String getTipoConsultaEspecial() {
        return tipoConsultaEspecial;
    }

    public String getPlanoSaudeEspecial() {
        return planoSaudeEspecial;
    }

    public int getNumeroHorarios() {
        return numeroHorarios;
    }

    public ArrayList<TemplateConsultaAgenda> getListaDia() {
        return listaDia;
    }

    public UsuarioPaciente getUsuarioPacienteConsultaEspecial() {
        return usuarioPaciente;
    }

    public void insertDummyPaciente(String email, Activity activity, int selectedItemPosition, String tipoCOnsulta, String planoSaude, String celular, String nome) {
        tipoConsultaEspecial = tipoCOnsulta;
        planoSaudeEspecial = planoSaude;
        String erro = activity.getResources().getString(R.string.erro);

        if(!ValidationTest.verificaInternet(activity)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.internetSemConexao));
            return;
        }
        if(!ValidationTest.validaString(email)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.emailVazio));
            return;
        }
        if(!ValidationTest.validaEmail(email)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.emailInvalido));
            return;
        }

        if(!ValidationTest.validaString(nome)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.nomeVazio));
            return;
        }

        if(!ValidationTest.validaString(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celularVazio));
            return;
        }
        if(!ValidationTest.validaTelefone(celular)){
            DialogAux.dialogCarregandoSimplesDismiss();
            dialogOkSimples(activity,erro,activity.getResources().getString(R.string.celulaInvalido));
            return;
        }


        UsuarioPaciente usuario = new UsuarioPaciente(email,nome,"",celular,true);
        usuario.setEndereco(null);
        PacienteBC b1 = new PacienteBC();
        b1.insertPaciente(usuario,email,activity,selectedItemPosition);


        agendaBC.getPacienteViaMarcacaoConsulta(email, activity, selectedItemPosition + 1);
    }

    public void setUsuarioPacienteConsultaEspecial(UsuarioPaciente usuarioPacienteConsultaEspecial) {
        this.usuarioPaciente = usuarioPacienteConsultaEspecial;
    }


    public void limpaDados(Activity tela) {
        LinearLayout f1 = (LinearLayout)tela.findViewById(R.id.consultasDiarias);
        f1.removeAllViews();
        f1 = (LinearLayout)tela.findViewById(R.id.consultasDiarias2);
        f1.removeAllViews();
        f1 = (LinearLayout)tela.findViewById(R.id.consultasDiarias3);
        f1.removeAllViews();
    }
}












