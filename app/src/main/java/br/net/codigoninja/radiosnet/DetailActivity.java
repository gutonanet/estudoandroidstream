package br.net.codigoninja.radiosnet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.net.codigoninja.radiosnet.dao.RadioDAO;

/**
 * Created by guton on 27/01/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView mTextView;
    private  Bundle extras;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        mapeiaComponente();
        obtemExtras();
        iniciaRadio();

        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.fav);
        int favorito = extras.getInt("favorito");
        if(favorito == 0){
            myFab.setVisibility(View.VISIBLE);
        }else{
            myFab.setVisibility(View.INVISIBLE);
        }

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioDAO dao = new RadioDAO(getThis());
                dao.salvarFavorito(extras.getInt("id"), 1);
                String texto = "Radio adicionada aos favoritos.";
                int duracao = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getThis(), texto,duracao);
                toast.show();
            }
        });

        FloatingActionButton rem = (FloatingActionButton)  findViewById(R.id.rem);
        if(favorito == 1){
            rem.setVisibility(View.VISIBLE);
        }else{
            rem.setVisibility(View.INVISIBLE);
        }

        rem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioDAO dao = new RadioDAO(getThis());
                dao.salvarFavorito(extras.getInt("id"), 0);
                String texto = "Radio removida dos favoritos.";
                int duracao = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getThis(), texto,duracao);
                toast.show();
            }
        });

    }

    public Integer inverte(Integer favorito){
        if(favorito.equals(0)){
            return 1;
        }else{
            return 0;
        }
    }

    public Context getThis(){
        return this;
    }

    public void mapeiaComponente(){
        mTextView = (TextView)findViewById(R.id.id_text);


    }

    public void obtemExtras(){
        extras = getIntent().getExtras();
        if(extras!= null){
            mTextView.setText(extras.getString("nome"));
        }
    }

    public void iniciaRadio(){
        try{

            if(extras == null){
                Toast.makeText(DetailActivity.this, "Erro ao executar a radio",
                        Toast.LENGTH_SHORT).show();
            }

            String url = extras.getString("url");//"http://tuneinmix.crossradio.com.br:8008/stream?type=http&amp;nocache=74659"; // your URL here
            //String url = "http://........"; // your URL here

            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }else{
                mediaPlayer.stop();
            }


            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();

        }catch (Exception e ){
            Toast.makeText(DetailActivity.this, "Erro ao executar a radio",
                    Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

    }


}
