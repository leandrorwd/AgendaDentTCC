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
    private Endereco endereco;
    private boolean masculino;
    private long idPaciente;

    public UsuarioPaciente() {
    }

    public UsuarioPaciente(String email, String nome, String celular, String sobrenome, long idPaciente,boolean masculino) {
        this.email = email;
        this.nome = nome;
        this.celular = celular;
        this.sobreNome = sobrenome;
        this.idPaciente = idPaciente;
        this.masculino = masculino;
    }


    public UsuarioPaciente(String email, String nome, String sobreNome, String celular,boolean masculino) {
        this.email = email;
        this.nome = nome;
        this.celular = celular;
        this.sobreNome = sobreNome;
        this.masculino = masculino;
    }


    public UsuarioPaciente(DataSnapshot dataSnapshot) {
        this.email = String.valueOf(dataSnapshot.child("email").getValue());
        this.masculino = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("masculino").getValue()));
        this.nome = String.valueOf(dataSnapshot.child("nome").getValue());
        this.sobreNome = String.valueOf(dataSnapshot.child("sobrenome").getValue());
        this.celular = String.valueOf(dataSnapshot.child("celular").getValue());
        this.idPaciente = Long.parseLong(String.valueOf(dataSnapshot.child("idPaciente").getValue()));
        this.endereco = dataSnapshot.child("endereco").getValue(Endereco.class);
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

    public boolean isMasculino() {
        return masculino;
    }

    public void setMasculino(boolean masculino) {
        this.masculino = masculino;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
