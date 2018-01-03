package br.com.grupoccaa.projetolivroprofessor.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        }
    }

    public EstanteAdapter(ArrayList<Publicacao> datasetPublicacoes) {
        this.datasetPublicacoes = datasetPublicacoes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
