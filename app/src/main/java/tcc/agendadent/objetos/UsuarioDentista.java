package tcc.agendadent.objetos;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by natha on 06/02/2017.
 */

public class UsuarioDentista {
    private String email;
    private String nome;
    private String sobreNome;
    private String inscricaoCRO;
    private String urlFotoPerfil;
    private long idDentista;
    private Endereco endereco;
    private boolean status;
    //Falta Agenda

    public UsuarioDentista() {
    }

    public UsuarioDentista(DataSnapshot dataSnapshot){
        this.email =String.valueOf(dataSnapshot.child("email").getValue());
        this.nome =String.valueOf(dataSnapshot.child("nome").getValue());
        this.sobreNome = String.valueOf(dataSnapshot.child("sobreNome").getValue());
        this.inscricaoCRO = String.valueOf(dataSnapshot.child("inscricaoCRO").getValue());
        this.urlFotoPerfil = String.valueOf(dataSnapshot.child("urlFotoPerfil").getValue());
        this.endereco = dataSnapshot.child("endereco").getValue(Endereco.class);
        this.status = Boolean.parseBoolean(String.valueOf(dataSnapshot.child("status").getValue()));
        this.idDentista = Long.parseLong(String.valueOf(dataSnapshot.child("idDentista").getValue()));
        //Agenda
    }

    public UsuarioDentista(String email, String nome, String sobreNome, String inscricaoCRO, String urlFotoPerfil, Endereco endereco) {
        this.email = email;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.inscricaoCRO = inscricaoCRO;
        this.urlFotoPerfil = urlFotoPerfil;
        this.endereco = endereco;
        status = false;
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

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getInscricaoCRO() {
        return inscricaoCRO;
    }

    public void setInscricaoCRO(String inscricaoCRO) {
        this.inscricaoCRO = inscricaoCRO;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(long idDentista) {
        this.idDentista = idDentista;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioDentista that = (UsuarioDentista) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (nome != null ? !nome.equals(that.nome) : that.nome != null) return false;
        if (sobreNome != null ? !sobreNome.equals(that.sobreNome) : that.sobreNome != null)
            return false;
        if (inscricaoCRO != null ? !inscricaoCRO.equals(that.inscricaoCRO) : that.inscricaoCRO != null)
            return false;
        if (urlFotoPerfil != null ? !urlFotoPerfil.equals(that.urlFotoPerfil) : that.urlFotoPerfil != null)
            return false;
        return endereco != null ? endereco.equals(that.endereco) : that.endereco == null;

    }

}