package br.net.codigoninja.radiosnet.dto;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gutonanet on 04/03/18.
 */

public class FactoryJson {

    public static Genero parsinJson(String result){
        try {
            JSONArray jsonObject = new JSONArray(result.toString());
            JSONObject obj = jsonObject.getJSONObject(0);
            Integer id = obj.getInt("id");
            String nome = obj.getString("nome");
            return new Genero(id, nome);
        }catch(Exception e){
            return null;

        }

    }

}
