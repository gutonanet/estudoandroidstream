package br.net.codigoninja.radiosnet.dto;

/**
 * Created by guton on 25/01/2018.
 */

public class Radio {

    private String nome;

    private String url;

    public Radio(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return nome;
    }
}
