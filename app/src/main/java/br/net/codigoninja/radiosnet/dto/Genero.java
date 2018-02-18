package br.net.codigoninja.radiosnet.dto;

/**
 * Created by gutonanet on 18/02/2018.
 */

public class Genero {

    private Integer id;

    private String nome;

    public Genero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genero genero = (Genero) o;

        return nome.equals(genero.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
