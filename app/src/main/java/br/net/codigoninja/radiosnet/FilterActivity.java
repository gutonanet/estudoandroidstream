package br.net.codigoninja.radiosnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.net.codigoninja.radiosnet.dao.CidadeDAO;
import br.net.codigoninja.radiosnet.dao.GeneroDAO;
import br.net.codigoninja.radiosnet.dao.RadioDAO;
import br.net.codigoninja.radiosnet.dto.Radio;

/**
 * Created by guton on 13/02/2018.
 */

public class FilterActivity  extends AppCompatActivity {

    AutoCompleteTextView editCidade;

    AutoCompleteTextView editGenero;

    AutoCompleteTextView editNome;

    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        editNome = findViewById(R.id.editNome);
        lista = (ListView) findViewById(R.id.lista);
        //CIDADES
        CidadeDAO dao = new CidadeDAO(this);
        String[] cidades = dao.retornoNomes();
        editCidade = (AutoCompleteTextView) findViewById(R.id.editCidade);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, cidades);
        //Used to specify minimum number of
        //characters the user has to type in order to display the drop down hint.
        editCidade.setThreshold(1);
        //Setting adapter
        editCidade.setAdapter(arrayAdapter);

        //Genero
        GeneroDAO gdao = new GeneroDAO(this);
        String[] generos = gdao.retornoNomes();
        editGenero = (AutoCompleteTextView) findViewById(R.id.editGenero);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, generos);
        //Used to specify minimum number of
        //characters the user has to type in order to display the drop down hint.
        editGenero.setThreshold(1);
        //Setting adapter
        editGenero.setAdapter(arrayAdapter2);


        //Nome Radio
        String[] nomes = {};
        editNome = (AutoCompleteTextView) findViewById(R.id.editNome);
        editNome.addTextChangedListener(textChecker);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Radio r = (Radio)parent.getAdapter().getItem(position);

                Intent intent = new Intent(FilterActivity.this, DetailActivity.class);
                Bundle extra = new Bundle();
                extra.putInt("id",r.getId());
                extra.putString("nome",r.getNome());
                extra.putString("url",r.getUrl());
                extra.putInt("favorito",r.getFavorito());
                intent.putExtras(extra);
                startActivity(intent);


            }  });



    }

    ArrayAdapter<String> arrayAdapter3 = null;
    final TextWatcher textChecker = new TextWatcher() {
        public void afterTextChanged(Editable s) {}

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            RadioDAO rdao = new RadioDAO(getThis());
            String cidade = editCidade.getText().toString();
            String genero = editGenero.getText().toString();
            String nome = editNome.getText().toString();
            List<Radio> radios = new ArrayList<>();
            if(nome != null && !"".equals(nome)){
               radios = rdao.retornarDados(cidade,genero,nome);
            }

            ArrayAdapter<Radio> adapter = new ArrayAdapter<Radio>(getThis(), android.R.layout.simple_list_item_1, radios);
            lista.setAdapter(adapter);
            //Used to specify minimum number of
            //characters the user has to type in order to display the drop down hint.


        }
    };

    public FilterActivity getThis(){
        return this;
    }



}
