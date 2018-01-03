package br.com.grupoccaa.projetolivroprofessor.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;

/**
 * Created by gerso on 1/3/2018.
 */

public class EstanteAdapter extends RecyclerView.Adapter<EstanteAdapter.ViewHolder> {

    private ArrayList<Publicacao> datasetPublicacoes;



    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imagePublicacao;
        TextView textViewTituloPublicacao;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imagePublicacao = (ImageView) itemView.findViewById(R.id.);
            this.textViewTituloPublicacao = textViewTituloPublicacao;
            this.cardView = cardView;
        }
    }







}
