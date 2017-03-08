package tcc.agendadent.gui.layout_auxiliares;

/**
 * Created by natha on 01/03/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import android.widget.TimePicker;

import tcc.agendadent.R;
import tcc.agendadent.controllers.DentistaController;



public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT
                ,this, 0, 0, DateFormat.is24HourFormat(getActivity()));

        TextView tvTitle = new TextView(getActivity());
        if(DentistaController.getInstance().isHorarioDuracao()){
            tvTitle.setText("Selecione a duração da consulta:");

        }
        else tvTitle.setText("Selecione o horario:");
        tvTitle.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvTitle.setPadding(5, 3, 5, 3);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tpd.setCustomTitle(tvTitle);
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        Activity aux = DentistaController.getInstance().getTelaAuxiliar();
        TextView textAux=null;
        String hora;
        String minuto;
        if(hourOfDay <10)
            hora = "0" + hourOfDay;
        else hora = hourOfDay +"";
        if(minute <10)
            minuto = "0" + minute;
        else minuto = minute +"";
        String result = hora + ":" +minuto;
        if(aux.getLocalClassName().equals("gui.dentista.ConfigAgendaAdiciona")) {
            if (DentistaController.getInstance().isHorarioInicio()) {
                textAux = (TextView) aux.findViewById(R.id.textoHorario);
            }
            if (DentistaController.getInstance().isHorarioTermino()) {
                textAux = (TextView) aux.findViewById(R.id.textoHorarioEnd);
            }
            if (DentistaController.getInstance().isHorarioDuracao()) {
                textAux = (TextView) aux.findViewById(R.id.minutosConsulta);
            }
            if (textAux != null)
                textAux.setText(result);
        }
    }
}
