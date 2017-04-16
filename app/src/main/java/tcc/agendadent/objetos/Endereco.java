package tcc.agendadent.objetos;

/**
 * Created by natha on 02/02/2017.
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Endereco implements Serializable {

    private String pais;

    private String estado;

    private String cidade;

    private String bairro;

    private String rua;

    private String complemento;

    private long numero;

    private int cep;

    public Endereco(){	}

    public Endereco(String pais, String estado, String cidade, String bairro, String rua, String complemento, int numero, int cep) {
        this.pais = pais;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.complemento = complemento;
        this.numero = numero;
        this.cep = cep;
    }

    public Endereco(JSONObject jsonObject) throws JSONException {
        this.pais = "Brasil";
        this.estado = jsonObject.getString("uf");
        this.cidade = jsonObject.getString("localidade");
        this.bairro = jsonObject.getString("bairro");
        this.rua = jsonObject.getString("logradouro");
        this.complemento = jsonObject.getString("complemento");
        this.numero = 0;
        this.cep = Integer.parseInt(jsonObject.getString("cep").replace("-", ""));
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getRuaNumero() {
        return rua + ", " + numero;
    }

    @Override
    public String toString() {
        return
                "Pais: '" + pais + '\'' +
                        ", Estado: '" + estado + '\'' +
                        ", Cidade: '" + cidade + '\'' +
                        ", Bairro :'" + bairro + '\'' +
                        ", Rua: '" + rua + '\'' +
                        ", Numero: " + numero +
                        ", Complemento: '" + complemento + '\'' +
                        ", Cep: " + cep +
                        '.';
    }
}