package br.net.codigoninja.radiosnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.net.codigoninja.radiosnet.adapter.AdapterImage;
import br.net.codigoninja.radiosnet.dao.CidadeDAO;
import br.net.codigoninja.radiosnet.dao.ControleDAO;
import br.net.codigoninja.radiosnet.dao.GeneroDAO;
import br.net.codigoninja.radiosnet.dao.RadioDAO;
import br.net.codigoninja.radiosnet.dto.Cidade;
import br.net.codigoninja.radiosnet.dto.FactoryJson;
import br.net.codigoninja.radiosnet.dto.Genero;
import br.net.codigoninja.radiosnet.dto.Radio;


public class MainActivity extends MenuActivity{

    private String cidade;
    private String genero;
    private String nomeRadio;
    private  Bundle extras;
    private TextView txtJson;





    @Override
    public void onRestart()
    {
        super.onRestart();
        onCreate();
    }

    protected void onCreate() {

        obtemExtras();

        txtJson = new TextView(MainActivity.this);

        ListView lista = (ListView) findViewById(R.id.lista);

        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pesquisar();
            }
        });
        Button btnAtualizar = (Button)  findViewById(R.id.btnAtualizar);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                executarAtualizar();
            }
        });


        List<Radio> radios = todosAsRadios();
        AdapterImage adapter = new AdapterImage(radios,this);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Radio r = (Radio)parent.getAdapter().getItem(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle extra = new Bundle();
                extra.putInt("id",r.getId());
                extra.putString("nome",r.getNome());
                extra.putString("url",r.getUrl());
                extra.putInt("favorito",r.getFavorito());
                intent.putExtras(extra);
                startActivity(intent);


            }  });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreate();

    }


    public void obtemExtras(){
        cidade = null;
        genero = null;
        nomeRadio = null;
        extras = getIntent().getExtras();
        if(extras!= null){
            cidade = extras.getString("cidade");
            genero = extras.getString("genero");
            nomeRadio = extras.getString("nomeRadio");
        }
    }

    /**
     * Exemplo qualquer de devolução de uma lista de cursos.
     * Para esse exemplo será considerado um hard coded.
     *
     * @return lista com todos os cursos
     */
    private List<Radio> todosAsRadios() {
        carregaDados();
        RadioDAO dao = new RadioDAO(this);
        return dao.retornarDados(cidade, genero, nomeRadio);
    }

    private void carregaDados(){
        CidadeDAO cidadeDAO = new CidadeDAO(this);
        cidadeDAO.carregaDados(this);
        GeneroDAO generoDAO = new GeneroDAO(this);
        generoDAO.carregaDados(this);
        RadioDAO dao = new RadioDAO(this);
        dao.carregaDados(this);
        ControleDAO controleDAO = new ControleDAO(this);
        controleDAO.carregaDados(this);
    }




}

