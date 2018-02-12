package br.net.codigoninja.radiosnet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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



    public List<Radio> retornarTodos(){
        List<Radio> radios = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Radios", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String url = cursor.getString(cursor.getColumnIndex("URL"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));

            radios.add(new Radio(id, nome, url, uf));
        }
        cursor.close();
        return radios;
    }

    public void carregaRadios(Context cx){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT count(*) as valor FROM Radios", null);
        int valor = 0;
        if(cursor.moveToNext()){
            valor = cursor.getInt(cursor.getColumnIndex("valor"));
        }
        if (valor == 0){
            populaTabela(cx);
        }
    }


    public boolean salvar(Integer id, String nome, String url, String uf){
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("Nome", nome);
        cv.put("Url", url);
        cv.put("UF", uf);

        return gw.getDatabase().insert(TABLE_RADIOS, null, cv) > 0;
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

            br = new BufferedReader(new InputStreamReader(cx.getAssets().open("dados.dat")));
            for (String linha = br.readLine(); linha != null; linha = br.readLine()) {
                String[] campos = linha.split(";");
                Integer id = Integer.valueOf(campos[0]);
                String nome = campos[1];
                String url = campos[2];
                String uf = campos[3];
                this.salvar(id,nome,url,uf);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

}