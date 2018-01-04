package br.com.grupoccaa.projetolivroprofessor.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;

import br.com.grupoccaa.projetolivroprofessor.R;
import br.com.grupoccaa.projetolivroprofessor.activities.ReaderActivity;
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
        public Context context;


        public ViewHolder(final View itemView) {
            super(itemView);

            tituloPublicacao = (TextView) itemView.findViewById(R.id.textViewTituloPublicacao);
            thumbnailPublicacao = (ImageView) itemView.findViewById(R.id.thumbnailPublicacao);
            context = itemView.getContext();
           }
    }

    public EstanteAdapter(Context context, List<Publicacao> listPublicacoes) {
        this.context = context;
        this.listPublicacoes = listPublicacoes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_publicacao,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Publicacao publicacao = listPublicacoes.get(position);
        holder.tituloPublicacao.setText(publicacao.getTituloPublicacao());


        Uri uri = Uri.parse(publicacao.getUrlThumbnail());
        final Context context = holder.thumbnailPublicacao.getContext();
        Glide.with(context)
                .load(uri)
                .into(holder.thumbnailPublicacao);

        holder.thumbnailPublicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putParcelable("publicacao", publicacao);
                Intent i = new Intent(context, ReaderActivity.class);
                i.putExtras(args);
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listPublicacoes.size();
    }


}


