package br.net.codigoninja.radiosnet;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by gutonanet on 06/05/18.
 */

public class BackgroundSoundService extends Service {

    private static final String TAG = "BackgroundSoundService";
    private MediaPlayer player;
    private ProgressDialog pd;

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate(){
        try {
            super.onCreate();
            player = new MediaPlayer();

            player.setVolume(100, 100);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setLooping(true); // Set looping
            player.setDataSource(DetailActivity.URL_RADIO);
            player.prepareAsync(); // might take long! (for buffering, etc)

            Log.i(TAG, "onCreate() , service started...");
        }catch(Exception e){
            new RuntimeException(e);
        }

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
                if (pd.isShowing()){
                    pd.dismiss();
                }


            }
        });

        return Service.START_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        Log.i(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        Toast.makeText(this, "Service stopped...", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }
}
