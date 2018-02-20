package br.net.codigoninja.radiosnet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.net.codigoninja.radiosnet.dto.Radio;

/**
 * Created by guton on 12/02/2018.
 */

public class RadioDAO {

    private final String TABLE_RADIOS = "Radios";
    private DbGateway gw;

    public RadioDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
      }


    public String[] retornaNomes(String cidade, String genero, String nomeRadio){
        List<Radio> lista = retornarDados(cidade,genero,nomeRadio);
        String[] radios = new String[lista.size()];
        int i = 0;
        for(Radio r:lista){
            radios[i] = r.getNome();
            i++;
        }
        return radios;
    }


    public List<Radio> retornarDados(String cidade, String genero, String nomeRadio){
        String query = " SELECT r.* FROM Radios r ";
        query += " INNER JOIN Cidades c on (c.ID = r.ID_CIDADE) ";
        query += " INNER JOIN Generos g on (g.ID = r.ID_GENERO) ";
        String where = " where 1=1 ";
        List<String> argumentos = new ArrayList<>();
        boolean semParametro = true;
        if(cidade!= null && !"".equals(cidade)){

            where += " AND c.NOME = ? AND c.UF = ? ";
            String[] dados = cidade.split("-");
            argumentos.add(dados[0].trim());
            argumentos.add(dados[1].trim());
            semParametro = false;
        }

        if(genero!= null  && !"".equals(genero)){

            where += " AND g.NOME = ? ";
            argumentos.add(genero.trim());
            semParametro = false;
        }

        if(nomeRadio != null && !"".equals(nomeRadio)){
            where += " AND r.NOME like ? ";
            argumentos.add("%"+nomeRadio.trim()+"%");
            semParametro = false;

        }

        String[] args = null;
        if(!argumentos.isEmpty()){
            args = new String[argumentos.size()];
            for(int i = 0; i < argumentos.size(); i++){
                args[i] = argumentos.get(i);
            }

        }

        if(semParametro){
            where += " AND r.FAVORITO = 1 ";
        }
        List<Radio> radios = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery(query+where, args);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("NOME"));
            String url = cursor.getString(cursor.getColumnIndex("URL"));
            Integer idCidade = cursor.getInt(cursor.getColumnIndex("ID_CIDADE"));
            Integer idGereno = cursor.getInt(cursor.getColumnIndex("ID_GENERO"));
            Integer favorito = cursor.getInt(cursor.getColumnIndex("FAVORITO"));

            radios.add(new Radio(id, nome, url, idCidade, idGereno, favorito));
        }
        cursor.close();
        return radios;
    }

    public void carregaDados(Context cx){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT count(*) as valor FROM Radios", null);
        int valor = 0;
        if(cursor.moveToNext()){
            valor = cursor.getInt(cursor.getColumnIndex("valor"));
        }
        if (valor == 0){
            populaTabela(cx);
        }
    }


    public boolean salvar(Integer id, String nome, String url, Integer idCidade, Integer idGenero, Integer favorito){
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("Nome", nome);
        cv.put("Url", url);
        cv.put("ID_CIDADE", idCidade);
        cv.put("ID_GENERO", idGenero);
        cv.put("FAVORITO", favorito);

        return gw.getDatabase().insert(TABLE_RADIOS, null, cv) > 0;
    }

    public boolean salvarFavorito(Integer id,  Integer favorito){
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("FAVORITO", favorito);

        return gw.getDatabase().update(TABLE_RADIOS, cv,"ID=?", new String[]{ id + "" }) > 0;
    }



  /*
        return new ArrayList<>(Arrays.asList(
                new Radio(1,"Radio Mix FM SP", "http://tuneinmix.crossradio.com.br:8008/stream?type=http&amp;nocache=74659","SP"),
                new Radio(2,"Jovem Pan FM - 100.9 FM", "http://17483.live.streamtheworld.com:3690/JP_SP_FM_SC", "SP"),
                new Radio(3,"Top FM - 104.1", "http://18703.live.streamtheworld.com/TUPIFMAAC_SC", "SP")));
                */

    private void populaTabela(Context cx){
        BufferedReader br  = null;
        try {

            br = new BufferedReader(new InputStreamReader(cx.getAssets().open("radios.dat")));
            for (String linha = br.readLine(); linha != null; linha = br.readLine()) {
                String[] campos = linha.split("!");
                Integer id = Integer.valueOf(campos[0]);
                String nome = campos[1];
                String url = campos[2];
                Integer idCidade = Integer.valueOf(campos[3]);
                Integer idGenero = Integer.valueOf(campos[4]);
                Integer favorito = Integer.valueOf(campos[5]);
                this.salvar(id,nome,url,idCidade, idGenero, favorito);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

}