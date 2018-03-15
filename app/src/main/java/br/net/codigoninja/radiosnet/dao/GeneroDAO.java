package br.net.codigoninja.radiosnet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.net.codigoninja.radiosnet.dto.Cidade;
import br.net.codigoninja.radiosnet.dto.Genero;

/**
 * Created by gutonanet on 18/02/2018.
 */

public class GeneroDAO {

    private final String TABLE_GENEROS = "Generos";
    private DbGateway gw;

    public GeneroDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public String[] retornoNomes(){
        List<Genero> lista = retornarTodos();
        String[] nomes = new String[lista.size()];
        int i = 0;
        for(Genero genero:lista){
            String nome = genero.getNome();
            nomes[i] = nome;
            i++;
        }
        return nomes;
    }



    public List<Genero> retornarTodos(){
        List<Genero> generos = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Generos", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("NOME"));

            generos.add(new Genero(id, nome));
        }
        cursor.close();
        return generos;
    }

    public void carregaDados(Context cx){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT count(*) as valor FROM Generos", null);
        int valor = 0;
        if(cursor.moveToNext()){
            valor = cursor.getInt(cursor.getColumnIndex("valor"));
        }
        if (valor == 0){
            populaTabela(cx);
        }
    }


    public boolean salvar(Integer id, String nome){
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("Nome", nome);

        return gw.getDatabase().insert(TABLE_GENEROS, null, cv) > 0;
    }


     private void populaTabela(Context cx){
        BufferedReader br  = null;
        try {

            br = new BufferedReader(new InputStreamReader(cx.getAssets().open("generos.dat")));
            for (String linha = br.readLine(); linha != null; linha = br.readLine()) {
                String[] campos = linha.split(";");
                Integer id = Integer.valueOf(campos[0]);
                String nome = campos[1];
                this.salvar(id,nome);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }
}
