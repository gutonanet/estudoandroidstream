package br.net.codigoninja.radiosnet.dto;

/**
 * Created by gutonanet on 18/02/2018.
 */

public class Cidade {

    private Integer id;

    private String nome;

    private String uf;

    public Cidade(int id, String nome, String uf) {
        this.id = id;
        this.nome = nome;
        this.uf = uf;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNomeApresentacao(){
        if(nome != null && uf != null){
            return nome +" - "+uf;
        }

        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cidade cidade = (Cidade) o;

        if (!nome.equals(cidade.nome)) return false;
        return uf.equals(cidade.uf);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + uf.hashCode();
        return result;
    }
}
