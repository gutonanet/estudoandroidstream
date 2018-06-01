package br.net.codigoninja.radiosnet;

import android.support.v7.app.ActionBar ;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.widget.SearchView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

/**
 * Created by gutonanet on 06/05/18.
 */

public abstract class MenuActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterView.OnItemClickListener {

    private String tipoAcesso;

    private SearchView searchView;

    // Get SearchView autocomplete object.
    private SearchView.SearchAutoComplete searchAutoComplete;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        // Get SearchView autocomplete object.
        searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchAutoComplete.setBackgroundColor(Color.WHITE);w
        //searchAutoComplete.setTextColor(Color.);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ActionBar actionBar = getSupportActionBar();
        List<Radio> radios = new ArrayList<>();
        RadioDAO rdao = new RadioDAO(getThis());
        String[] resultado  = rdao.retornaNomes(null,null,newText, false);

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, resultado);
        searchAutoComplete.setAdapter(newsAdapter);
        searchAutoComplete.setOnItemClickListener(this);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
        String queryString=(String)adapterView.getItemAtPosition(itemIndex);
        searchAutoComplete.setText("" + queryString);
        RadioDAO rdao = new RadioDAO(getThis());
        List<Radio> radios = rdao.retornarDados(null,null,queryString);

        Radio r = (Radio)radios.get(0);

        Intent intent = new Intent(MenuActivity.this, DetailActivity.class);
        Bundle extra = new Bundle();
        extra.putInt("id",r.getId());
        extra.putString("nome",r.getNome());
        extra.putString("url",r.getUrl());
        extra.putInt("favorito",r.getFavorito());
        intent.putExtras(extra);
        startActivity(intent);

    }


/*
ListView(MainActivity.this);
lv.setAdapter(new CategoryAdapter(getApplicationContext(), tv.categories));
RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT); params.addRule(RelativeLayout.BELOW,login_button.getId()); inflating_view = findViewById(R.id.inflating_view); ViewGroup parent =(ViewGroup) inflating_view.getParent(); parent.addView(lv,params);
 */


    public MenuActivity getThis(){
        return this;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.favoritos:
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.pesquisar:
                pesquisar();
                break;
            case R.id.atualizar:
                executarAtualizar();
                break;

        }
        return true;
    }

    protected void executarAtualizar(){
        ControleDAO d = new ControleDAO(MenuActivity.this);
        String data = d.retornaDataControle();
        JsonTask j1 = new JsonTask();
        j1.execute("http://192.168.0.8:8080/RadiosProject/rest/appService/findGeneros/"+data,"G");

        JsonTask j2 = new JsonTask();
        j2.execute("http://192.168.0.8:8080/RadiosProject/rest/appService/findCidades/"+data,"C");
        // j.execute("http://192.168.0.14:8080/RadiosProject/rest/appService/findRadios/"+data,"R");
    }

    protected void pesquisar(){
        Intent intent = new Intent(MenuActivity.this, FilterActivity.class);
        startActivity(intent);
    }

    //Botão que irá atualizar as radios pela internet
    protected class JsonTask extends AsyncTask<String, String, String> {
        private ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

            if(pd == null) {
                pd = new ProgressDialog(MenuActivity.this);
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
                    GeneroDAO dao = new GeneroDAO(MenuActivity.this);
                    dao.salvar(g.getId(), g.getNome());
                }

            }
            if(tipoAcesso.equals("C")) {

                Cidade c = FactoryJson.parsingCidade(result);
                if (c != null) {
                    CidadeDAO dao = new CidadeDAO(MenuActivity.this);
                    dao.salvar(c.getId(), c.getNome(), c.getUf());
                }

            }
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }

}
