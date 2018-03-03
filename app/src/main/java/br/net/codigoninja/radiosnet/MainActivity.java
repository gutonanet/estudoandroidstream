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
import br.net.codigoninja.radiosnet.dao.GeneroDAO;
import br.net.codigoninja.radiosnet.dao.RadioDAO;
import br.net.codigoninja.radiosnet.dto.Radio;


public class MainActivity extends AppCompatActivity {

    private String cidade;
    private String genero;
    private String nomeRadio;
    private  Bundle extras;
    private TextView txtJson;
    private ProgressDialog pd;


    @Override
    public void onRestart()
    {
        super.onRestart();
        onCreate();
    }

    protected void onCreate() {

        obtemExtras();

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
                new JsonTask().execute("Url address here");
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
    }


    //Botão que irá atualizar as radios pela internet
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            txtJson.setText(result);
        }
    }

}

