package tcc.agendadent.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.AgendaBC;
import tcc.agendadent.gui.layout_auxiliares.TemplateConsultaAgenda;
import tcc.agendadent.objetos.AgendaSub;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.Horario;
import tcc.agendadent.servicos.DialogAux;

/**
 * Created by Work on 18/03/2017.
 */

public class AgendaController {
    private static AgendaController INSTANCE;
    private AgendaBC agendaBC;

    private AgendaController(){
        agendaBC = new AgendaBC();
    }

    public static AgendaController getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AgendaController();
        }
        return INSTANCE;
    }


    public void insertSubAgenda(AgendaSub ag){
        agendaBC.insertAgendaSub(ag,DentistaController.getInstance().getDentistaLogado());
    }

    public void insertConsulta(Consulta consulta,String idDentista,String semestreAno){
        agendaBC.insertConsulta(consulta,idDentista,semestreAno);
    }

    public void getConsultasSemestre(long idDentista, String anoSemestre, Activity tela,boolean banco) {
        if(!banco){
            agendaBC.getConsultaSemestre(idDentista,anoSemestre,tela);
        }
    }

    public static void setAgendaDiaria(Activity tela, ArrayList<Consulta> consultas) {
        Collections.sort(DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios());
        ArrayList<Horario> horarios= new ArrayList<>();
        int indexHoje = DateTime.now().dayOfWeek().get()-1;
        for(Horario h :DentistaController.getInstance().getDentistaLogado().getAgenda().getHorarios() ){
            if(h.getDiasSemana().get(indexHoje)){
                horarios.add(h);
            }
        }
        ArrayList<Consulta> consultasHoje = new ArrayList<>();
        for(Consulta c : consultas){
            if((c.getDataFormat().dayOfYear().equals(DateTime.now().dayOfYear())
            && c.getDataFormat().getYear() == DateTime.now().getYear())){
                consultasHoje.add(c);
            }
        }
        populaAgendaDiaria(tela,horarios,consultasHoje);
    }

    private static void populaAgendaDiaria(Activity tela,ArrayList<Horario> horarios,ArrayList<Consulta> consultasMarcadas) {
        LinearLayout horarioDiario = (LinearLayout) tela.findViewById(R.id.consultasDiarias);
        boolean inseriu;
        for(Horario horario : horarios){
            inseriu = false;
            String horaInicial =horario.getHoraInicial();
            ArrayList<String> horariosAgenda = new ArrayList<>();
            int horaMinuto = 1;
            String[] hFinal =   horario.getHoraFinal().split(":");
            String[] parts =   horario.getDuracao().split(":");
            int horaFinal = 60*(Integer.parseInt(hFinal[0]) + Integer.parseInt(hFinal[1]));
            horariosAgenda.add(horaInicial);

            while(horaMinuto<horaFinal-Integer.parseInt(parts[1])){
                String[] parts2 =   horaInicial.split(":");
                int novaHora = (Integer.parseInt(parts[0]) + Integer.parseInt(parts2[0]));
                int novoMinuto = Integer.parseInt(parts[1]) + Integer.parseInt(parts2[1]);

                if(novoMinuto>=60){
                    novoMinuto = novoMinuto -60;
                    novaHora = novaHora+1;
                }
                horaMinuto = novaHora*60+novoMinuto;
                String snovaHora = novaHora+"";
                String snovoMinuto = novoMinuto+"";
                if(novaHora<10){
                    snovaHora = "0" +novaHora ;
                }
                if(novoMinuto<10){
                    snovoMinuto = "0" +novoMinuto;
                }
                horaInicial = snovaHora +":"+snovoMinuto;
                horariosAgenda.add(horaInicial);
            }
            DateTimeFormatter dateTimeFormatHora = DateTimeFormat.forPattern("HH:mm");
            for(String s : horariosAgenda){
                boolean entrou = false;
                Consulta aux=null;
                for(Consulta c1 : consultasMarcadas){
                    if(horariosAgenda.contains(dateTimeFormatHora.print(c1.getDataFormat()))){
                        if(dateTimeFormatHora.print(c1.getDataFormat()).equals(s)){
                            TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela,c1);
                            horarioDiario.addView(t1);
                            entrou = true;
                            aux = c1;
                            break;
                        }
                    }
                }
                if(!entrou){
                    TemplateConsultaAgenda t1 = new TemplateConsultaAgenda(tela,s);
                    horarioDiario.addView(t1);
                }
                else{
                    consultasMarcadas.remove(aux);
                }
            }
            horariosAgenda.clear();
        }
        DialogAux.dialogCarregandoSimplesDismiss();
    }
}


