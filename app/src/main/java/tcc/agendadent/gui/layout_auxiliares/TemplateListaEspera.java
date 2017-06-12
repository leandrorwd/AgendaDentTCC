package tcc.agendadent.gui.layout_auxiliares;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import tcc.agendadent.R;
import tcc.agendadent.controllers.PacienteController;
import tcc.agendadent.objetos.Consulta;
import tcc.agendadent.objetos.UsuarioDentista;

/**
 * Created by natha on 25/05/2017.
 */
//paciente_esperaConsulta
public class TemplateListaEspera extends LinearLayout {
    private Activity activity;
    private Consulta consulta;
    private UsuarioDentista dentista;

    public TemplateListaEspera(Activity tela, Consulta consulta) {
        super(tela);
        this.activity=tela;
        this.consulta = consulta;
        View.inflate(tela, R.layout.paciente_esperaconsulta, this);
        instanciaArtefatos();
        PacienteController.getInstance().getDentistaViaId(activity,consulta.getIdDentista(),this);
    }

    private void instanciaArtefatos() {
        TextView textoPacienteData = (TextView) findViewById(R.id.textoPacienteData);
        DateTimeFormatter formatData = DateTimeFormat.forPattern("dd/MM");
        DateTime data = new DateTime(consulta.getDataConsulta());
        textoPacienteData.setText(formatData.print(data));
        TextView textoPacienteHoraInicial = (TextView) findViewById(R.id.textoPacienteHoraInicial);
        DateTimeFormatter formatHorario = DateTimeFormat.forPattern("HH:mm");
        DateTime hora = new DateTime(consulta.getDataConsulta());
        DateTime aux = DateTime.now();
        DateTime horaCorrida;
        try {
            String operador = aux.toString().substring(23, 26);
            int valor = Integer.parseInt(aux.toString().substring(24, 26));
            if (operador.contains("+")) {
                horaCorrida = hora.minusHours(valor);
            } else {
                horaCorrida = hora.plusHours(valor);
            }
        } catch (Exception e) {
            horaCorrida = hora;
        }
        textoPacienteHoraInicial.setText(formatHorario.print(horaCorrida));
        TextView textoNomeDentista = (TextView) findViewById(R.id.textoNomeDentista);
        textoNomeDentista.setText( "Carregando");
        TextView textoEnderecoDentista = (TextView) findViewById(R.id.textoEnderecoDentista);
        textoEnderecoDentista.setText( "Carregando");
    }

    public void setDentista(UsuarioDentista dentista) {
        this.dentista = dentista;
        if (dentista.isMasculino()) {
            TextView textoNomeDentista = (TextView) findViewById(R.id.textoNomeDentista);
            textoNomeDentista.setText( "Dr. " + dentista.getNome() + " " + dentista.getSobreNome());
        } else {
            TextView textoNomeDentista = (TextView) findViewById(R.id.textoNomeDentista);
            textoNomeDentista.setText(  "Dra. " + dentista.getNome() + " " + dentista.getSobreNome());
        }

        TextView textoEnderecoDentista = (TextView) findViewById(R.id.textoEnderecoDentista);
        textoEnderecoDentista.setText(dentista.getEndereco().getRuaNumero());

    }
}
