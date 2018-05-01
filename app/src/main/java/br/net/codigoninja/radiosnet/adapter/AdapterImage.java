package br.net.codigoninja.radiosnet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.net.codigoninja.radiosnet.R;
import br.net.codigoninja.radiosnet.dto.Radio;

/**
 * Created by gutonanet on 22/04/18.
 */

public class AdapterImage extends BaseAdapter {

    private final List<Radio> radios;
    private final Activity act;

    public AdapterImage(List<Radio> radios, Activity act){
        this.radios = radios;
        this.act = act;

    }

    @Override
    public int getCount() {
        return radios.size();
    }

    @Override
    public Object getItem(int position) {
        return radios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return radios.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = act.getLayoutInflater()
                .inflate(R.layout.lista_radios, parent, false);


        Radio radio = radios.get(position);


        TextView descricao = (TextView)
                view.findViewById(R.id.lista_radios);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.lista_imagens);

        descricao.setText(radio.getNome());
        imagem.setImageResource(R.mipmap.radiorecordlogo);

        return view;
    }

}
