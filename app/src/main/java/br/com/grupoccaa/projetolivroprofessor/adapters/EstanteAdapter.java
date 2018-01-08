package br.com.grupoccaa.projetolivroprofessor.adapters;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import br.com.grupoccaa.projetolivroprofessor.R;
import br.com.grupoccaa.projetolivroprofessor.activities.ReaderActivity;
import br.com.grupoccaa.projetolivroprofessor.helper_classes.ExtraiFilenameDaUrl;
import br.com.grupoccaa.projetolivroprofessor.helper_classes.VerificaArquivosNoDispositivo;
import br.com.grupoccaa.projetolivroprofessor.models.Download;
import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;
import br.com.grupoccaa.projetolivroprofessor.services.DownloadService;

import static br.com.grupoccaa.projetolivroprofessor.activities.ReaderActivity.MESSAGE_PROGRESS;

/**
 * Created by Gerson Cardoso on 1/3/2018.
 */

public class EstanteAdapter extends RecyclerView.Adapter<EstanteAdapter.ViewHolder> {

    private List<Publicacao> listPublicacoes;
    private Context context;
    private ExtraiFilenameDaUrl extraiFilenameDaUrl;

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView thumbnailPublicacao;
        public TextView tituloPublicacao, progressText;
        public ProgressBar progressBar;
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

        holder.thumbnailPublicacao.setImageResource(R.drawable.pec5_lp_cover);

        /*Glide.with(context)
                .load(uri)
                .into(holder.thumbnailPublicacao);*/

        holder.thumbnailPublicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*File filename = new File(extraiFilenameDaUrl.GetFilename(publicacao.getUrlPublicacao()));

                File sdDir = Environment.getExternalStorageDirectory();
                File directory = new File(sdDir,"/input_files/");

                for(File f:directory.listFiles()){
                    if (filename.isFile()){
                        Bundle args = new Bundle();
                        args.putParcelable("publicacao", publicacao);
                        Intent i = new Intent(context, ReaderActivity.class);
                        i.putExtras(args);
                        context.startActivity(i);
                    } else {
                        Log.i("Download file===>", filename.toString());
                        Intent intent = new Intent(context, DownloadService.class);
                        intent.putExtra("urlPdf", publicacao.getUrlPublicacao());
                        context.startService(intent);
                    }

                }*/


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


