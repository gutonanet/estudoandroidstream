package br.net.codigoninja.radiosnet.controller;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

/**
 * Created by gutonanet on 02/06/2018.
 */

public class RadioController extends MediaController {
    public RadioController(Context context) {
        super(context);
    }

    public void hide(){}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                ((Activity) getContext()).onBackPressed();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
