package br.net.codigoninja.radiosnet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.net.codigoninja.radiosnet.dto.Cidade;
import br.net.codigoninja.radiosnet.dto.Radio;

/**
 * Created by gutonanet on 18/02/2018.
 */

public class CidadeDAO {

    private final String TABLE_CIDADES = "Cidades";
    private DbGateway gw;

    public CidadeDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public String[] retornoNomes(){
        List<Cidade> lista = retornarTodos();
        String[] nomes = new String[1];
        int i = 0;
        for(Cidade cidade:lista){
            String nome = cidade.getNomeApresentacao();
            nomes[i] = nome;
            i++;
        }
        return nomes;
    }

    public List<Cidade> retornarTodos(){
        List<Cidade> cidades = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Cidades", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("NOME"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));

            cidades.add(new Cidade(id, nome, uf));
        }
        cursor.close();
        return cidades;
    }

    public void carregaDados(Context cx){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT count(*) as valor FROM Cidades", null);
        int valor = 0;
        if(cursor.moveToNext()){
            valor = cursor.getInt(cursor.getColumnIndex("valor"));
        }
        if (valor == 0){
            populaTabela(cx);
        }
    }


    public boolean salvar(Integer id, String nome, String uf){
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("Nome", nome);
        cv.put("UF", uf);


        return gw.getDatabase().insert(TABLE_CIDADES, null, cv) > 0;
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

            br = new BufferedReader(new InputStreamReader(cx.getAssets().open("cidades.dat")));
            for (String linha = br.readLine(); linha != null; linha = br.readLine()) {
                String[] campos = linha.split(";");
                Integer id = Integer.valueOf(campos[0]);
                String nome = campos[1];
                String uf = campos[2];
                this.salvar(id,nome,uf);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

}
