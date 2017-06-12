package tcc.agendadent.controllers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import tcc.agendadent.R;
import tcc.agendadent.bancoConnection.EventosBC;
import tcc.agendadent.gui.paciente.PacienteMarcaConsulta;
import tcc.agendadent.objetos.Consulta;

/**
 * Created by Work on 22/05/2017.
 */

public class EventosController {
    private static EventosController INSTANCE;
    private EventosBC eventosBC;

    private EventosController(){
        eventosBC = new EventosBC();
    }
    private Activity activity;

    public static EventosController getInstance(){
        if(INSTANCE == null){
            INSTANCE = new EventosController();
        }
        return INSTANCE;
    }


    public void getKeyConsulta(Consulta consulta,Activity activity) {
        this.activity = activity;
        eventosBC.getKeyConsulta(consulta);
    }

    public void eventoConsulta(Consulta consulta) {
        for(Consulta consultaMarcada : AgendaController.getInstance().getProximasConsultas()){
            if(consultaMarcada.getIdDentista()==consulta.getIdDentista()){
                if((consultaMarcada.getDataConsulta()+"").equals(consulta.getDataConsulta()+"")){
                    return;
                }
            }
        }
        PacienteController.getInstance().setConsultaNotificao(consulta);
        Intent intent = new Intent(activity, PacienteMarcaConsulta.class);
        intent.putExtra("consulta", consulta);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notificacao = new Notification.Builder(activity)
                .setContentTitle("AgendaDent - Aviso")
                .setContentText("O horario que você estava esperando está livre!")
                .setStyle(new Notification.BigTextStyle()
                        .bigText("O horario que você estava esperando está livre!\n" +
                                "Clique aqui para analisar todos os dados da consulta."))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificacao.setTicker("ticker");

        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacao.build());

    }


    public void eventoConsultaDesMarc(Consulta consulta) {
        for(Consulta consultaMarcada : AgendaController.getInstance().getProximasConsultas()){
            if(consultaMarcada.getIdDentista()==consulta.getIdDentista()){
                if((consultaMarcada.getDataConsulta()+"").equals(consulta.getDataConsulta()+"")){
                    return;
                }
           }
        }
        PacienteController.getInstance().setConsultaNotificao(consulta);
        Intent intent = new Intent(activity, PacienteMarcaConsulta.class);
        PacienteController.getInstance().setConsultaNotificaoDesmarc(consulta);

        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notificacao = new Notification.Builder(activity)
                .setContentTitle("AgendaDent - Aviso")
                .setContentText("Um horario mais cedo foi liberado.")
                .setStyle(new Notification.BigTextStyle()
                        .bigText("Um horario mais cedo foi liberado.\n" +
                                "Clique aqui para analisar todos os dados da consulta."))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher) .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificacao.setTicker("ticker");

        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacao.build());

    }

    public void carregaListenersEspera(Activity activity) {
        AgendaController.getInstance().getConsultasListaEspera(activity);
    }

    public void carregaListenersEspera(Activity activity, ArrayList<String> indices) {
        this.activity = activity;
        eventosBC.getConsultasListaEspera(activity,indices);

    }


    public void carregaListenersRemarcacao() {
        for(Consulta c :AgendaController.getInstance().getProximasConsultas() ){
            eventosBC.setListenerConsulta(c,activity);
        }


    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }
}
