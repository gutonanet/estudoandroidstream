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

import br.net.codigoninja.radiosnet.dao.CidadeDAO;
import br.net.codigoninja.radiosnet.dao.ControleDAO;
import br.net.codigoninja.radiosnet.dao.GeneroDAO;
import br.net.codigoninja.radiosnet.dao.RadioDAO;
import br.net.codigoninja.radiosnet.dto.Cidade;
import br.net.codigoninja.radiosnet.dto.FactoryJson;
import br.net.codigoninja.radiosnet.dto.Genero;
import br.net.codigoninja.radiosnet.dto.Radio;


public class MainActivity extends AppCompatActivity {

    private String cidade;
    private String genero;
    private String nomeRadio;
    private  Bundle extras;
    private TextView txtJson;

    private String tipoAcesso;



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
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });
        Button btnAtualizar = (Button)  findViewById(R.id.btnAtualizar);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ControleDAO d = new ControleDAO(MainActivity.this);
                String data = d.retornaDataControle();
                JsonTask j1 = new JsonTask();
                j1.execute("http://192.168.0.8:8080/RadiosProject/rest/appService/findGeneros/"+data,"G");

                JsonTask j2 = new JsonTask();
                j2.execute("http://192.168.0.8:8080/RadiosProject/rest/appService/findCidades/"+data,"C");
               // j.execute("http://192.168.0.14:8080/RadiosProject/rest/appService/findRadios/"+data,"R");
            }
        });


        List<Radio> radios = todosAsRadios();
        ArrayAdapter<Radio> adapter = new ArrayAdapter<Radio>(this, android.R.layout.simple_list_item_1, radios);
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


    //Botão que irá atualizar as radios pela internet
    private class JsonTask extends AsyncTask<String, String, String> {
        private ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

            if(pd == null) {
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Aguarde");
                pd.setCancelable(false);
                pd.show();
            }
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            try {
                URL url = new URL(params[0]);
                tipoAcesso = params[1];
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));


                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(tipoAcesso.equals("G")) {

                Genero g = FactoryJson.parsingGenero(result);
                if (g != null) {
                    GeneroDAO dao = new GeneroDAO(MainActivity.this);
                    dao.salvar(g.getId(), g.getNome());
                }

            }
            if(tipoAcesso.equals("C")) {

                Cidade c = FactoryJson.parsingCidade(result);
                if (c != null) {
                    CidadeDAO dao = new CidadeDAO(MainActivity.this);
                    dao.salvar(c.getId(), c.getNome(), c.getUf());
                }

            }
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }

}

