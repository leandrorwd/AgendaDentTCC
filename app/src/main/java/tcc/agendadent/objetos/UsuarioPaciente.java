package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by natha on 08/02/2017.
 */

public class UsuarioPaciente {
    private String email;
    private String nome;
    private String celular;
    private String sobreNome;
    private long idPaciente;

    public UsuarioPaciente() {
    }

    public UsuarioPaciente(String email, String nome, String celular, String sobrenome, long idPaciente) {
        this.email = email;
        this.nome = nome;
        this.celular = celular;
        this.sobreNome = sobrenome;
        this.idPaciente = idPaciente;
    }


    public UsuarioPaciente(String email, String nome, String sobreNome, String celular) {
        this.email = email;
        this.nome = nome;
        this.celular = celular;
        this.sobreNome = sobreNome;
    }


    public UsuarioPaciente(DataSnapshot dataSnapshot){
        this.email =String.valueOf(dataSnapshot.child("email").getValue());
        this.nome =String.valueOf(dataSnapshot.child("nome").getValue());
        this.sobreNome = String.valueOf(dataSnapshot.child("sobreNome").getValue());
        this.idPaciente = Long.parseLong(String.valueOf(dataSnapshot.child("idPaciente").getValue()));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSobrenome() {
        return sobreNome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobreNome = sobrenome;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }
}
