package br.net.codigoninja.radiosnet;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

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
