package br.net.codigoninja.radiosnet.dto;

/**
 * Created by guton on 25/01/2018.
 */

public class Radio {

    private Integer id;

    private String nome;

    private String url;

    private String uf;

    public Radio(int id, String nome, String url, String uf) {
        this.id = id;
        this.nome = nome;
        this.url = url;
        this.uf = uf;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

    public String getUf() {
        return url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public String toString(){
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Radio radio = (Radio) o;

        return nome.equals(radio.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
