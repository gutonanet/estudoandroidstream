package br.net.codigoninja.radiosnet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.net.codigoninja.radiosnet.dto.Genero;

/**
 * Created by gutonanet on 14/03/18.
 */

public class ControleDAO {
    private final String TABLE_CONTROLE = "Controle";
    private DbGateway gw;

    public ControleDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public void carregaDados(Context cx){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT count(*) as valor FROM Controle", null);
        int valor = 0;
        if(cursor.moveToNext()){
            valor = cursor.getInt(cursor.getColumnIndex("valor"));
        }
        if (valor == 0){
            populaTabela(cx);
        }
    }

    public String retornaDataControle(){

        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Controle", null);
        if(cursor.moveToNext()){

            return cursor.getString(cursor.getColumnIndex("DATA"));

        }
        cursor.close();
        return null;
    }

    public boolean salvar(String data){
        ContentValues cv = new ContentValues();
        cv.put("ID", 1);
        cv.put("DATA", data);

        return gw.getDatabase().insert(TABLE_CONTROLE, null, cv) > 0;
    }


    private void populaTabela(Context cx){
        BufferedReader br  = null;
        try {

            br = new BufferedReader(new InputStreamReader(cx.getAssets().open("controle.dat")));
            for (String linha = br.readLine(); linha != null; linha = br.readLine()) {

                String data = linha.trim();

                this.salvar(data);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

}
