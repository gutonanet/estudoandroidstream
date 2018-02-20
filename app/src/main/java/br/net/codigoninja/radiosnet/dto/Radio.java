package br.net.codigoninja.radiosnet.dto;

/**
 * Created by guton on 25/01/2018.
 */

public class Radio {

    private Integer id;

    private String nome;

    private String url;

    private Integer idCidade;

    private Integer idGenero;

    private Integer favorito;



    public Radio(int id, String nome, String url, Integer idCidade, Integer idGenero, Integer favorito) {
        this.id = id;
        this.nome = nome;
        this.url = url;
        this.idCidade = idCidade;
        this.idGenero = idGenero;
        this.favorito = favorito;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(Integer idCidade) {
        this.idCidade = idCidade;
    }

    public Integer getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Integer idGenero) {
        this.idGenero = idGenero;
    }

    public Integer getFavorito() {
        return favorito;
    }

    public void setFavorito(Integer favorito) {
        this.favorito = favorito;
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
