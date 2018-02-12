package br.net.codigoninja.radiosnet;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.net.codigoninja.radiosnet.dao.RadioDAO;
import br.net.codigoninja.radiosnet.dto.Radio;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView lista = (ListView) findViewById(R.id.lista);

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
                    extra.putString("nome",r.getNome());
                    extra.putString("url",r.getUrl());
                    intent.putExtras(extra);
                    startActivity(intent);


            }  });

    }

    /**
     * Exemplo qualquer de devolução de uma lista de cursos.
     * Para esse exemplo será considerado um hard coded.
     *
     * @return lista com todos os cursos
     */
    private List<Radio> todosAsRadios() {
        /*
        return new ArrayList<>(Arrays.asList(
                new Radio(1,"Radio Mix FM SP", "http://tuneinmix.crossradio.com.br:8008/stream?type=http&amp;nocache=74659","SP"),
                new Radio(2,"Jovem Pan FM - 100.9 FM", "http://17483.live.streamtheworld.com:3690/JP_SP_FM_SC", "SP"),
                new Radio(3,"Top FM - 104.1", "http://18703.live.streamtheworld.com/TUPIFMAAC_SC", "SP")));
                */
        RadioDAO dao = new RadioDAO(this);

        dao.carregaRadios(this);
        return dao.retornarTodos();
    }
}

