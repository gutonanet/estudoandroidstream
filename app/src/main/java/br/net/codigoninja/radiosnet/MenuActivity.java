package br.net.codigoninja.radiosnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.net.codigoninja.radiosnet.dao.CidadeDAO;
import br.net.codigoninja.radiosnet.dao.ControleDAO;
import br.net.codigoninja.radiosnet.dao.GeneroDAO;
import br.net.codigoninja.radiosnet.dto.Cidade;
import br.net.codigoninja.radiosnet.dto.FactoryJson;
import br.net.codigoninja.radiosnet.dto.Genero;

/**
 * Created by gutonanet on 06/05/18.
 */

public abstract class MenuActivity extends AppCompatActivity {

    private String tipoAcesso;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
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
