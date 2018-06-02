package br.net.codigoninja.radiosnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import br.net.codigoninja.radiosnet.controller.RadioController;
import br.net.codigoninja.radiosnet.dao.RadioDAO;
import wseemann.media.FFmpegMediaPlayer;

/**
 * Created by guton on 27/01/2018.
 */

public class DetailActivity extends MenuActivity implements MediaController.MediaPlayerControl{

    private TextView mTextView;
    private  Bundle extras;
    private static FFmpegMediaPlayer mediaPlayer;
    private ProgressDialog pd;
    public static String URL_RADIO;

    private TextView mtextViewTitle;

    private RadioController controller;

    private void setController(){
        controller = new RadioController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.detalhe));
        controller.setEnabled(true);
        controller.setBackgroundColor(getResources().getColor(android.R.color.transparent));






    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        mapeiaComponente();
        obtemExtras();
        iniciaRadio();
        setController();


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

    //public Context getThis(){
    //    return this;
    //}

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
                mediaPlayer = new FFmpegMediaPlayer();
            }else{

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer = new FFmpegMediaPlayer();

                }

                //mediaPlayer.reset();
            }
            if(pd == null) {
                pd = new ProgressDialog(DetailActivity.this);
                pd.setMessage("Aguarde");
                pd.setCancelable(false);
                pd.show();
            }

            URL_RADIO = url;

            /*
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(url, new HashMap<String, String>());
            String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            mtextViewTitle = findViewById(R.id.title);
            mtextViewTitle.setText(title);
*/


            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            mediaPlayer.setOnPreparedListener(new FFmpegMediaPlayer.OnPreparedListener(){
                @Override
                public void onPrepared(FFmpegMediaPlayer mp) {
                    mediaPlayer.start();
                    controller.show(0);
                    if (pd.isShowing()){
                        pd.dismiss();
                    }


                }
            });

            new CountDownTimer(10000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    if(mediaPlayer == null){
                        return;
                    }
                    if(mediaPlayer.isPlaying()){
                        return;
                    }
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                    Toast.makeText(DetailActivity.this, "Erro ao executar a radio",
                            Toast.LENGTH_SHORT).show();
                    mediaPlayer = null;
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }.start();

        }catch (Exception e ){
            if(pd != null) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
            mediaPlayer = null;
            Toast.makeText(DetailActivity.this, "Erro ao executar a radio",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }
}
