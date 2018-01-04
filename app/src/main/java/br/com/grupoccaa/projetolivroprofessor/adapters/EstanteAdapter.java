package br.com.grupoccaa.projetolivroprofessor.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.grupoccaa.projetolivroprofessor.R;
import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;

/**
 * Created by gerso on 1/3/2018.
 */

public class EstanteAdapter extends RecyclerView.Adapter<EstanteAdapter.ViewHolder> {

    private List<Publicacao> listPublicacoes;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView thumbnailPublicacao;
        public TextView tituloPublicacao;


        public ViewHolder(View itemView) {
            super(itemView);

            tituloPublicacao = (TextView) itemView.findViewById(R.id.textViewTituloPublicacao);
            thumbnailPublicacao = (ImageView) itemView.findViewById(R.id.thumbnailPublicacao);

        }
    }

    public EstanteAdapter(Context context, List<Publicacao> listPublicacoes) {
        this.listPublicacoes = listPublicacoes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_publicacao,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Publicacao publicacao = listPublicacoes.get(position);
        holder.tituloPublicacao.setText(publicacao.getTituloPublicacao());

    }


    @Override
    public int getItemCount() {
        return listPublicacoes.size();
    }
}
