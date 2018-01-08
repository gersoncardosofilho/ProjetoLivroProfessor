package br.com.grupoccaa.projetolivroprofessor.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

import br.com.grupoccaa.projetolivroprofessor.R;
import br.com.grupoccaa.projetolivroprofessor.adapters.EstanteAdapter;
import br.com.grupoccaa.projetolivroprofessor.connection.RestClient;
import br.com.grupoccaa.projetolivroprofessor.helper_classes.GridSpacingItemDecoration;
import br.com.grupoccaa.projetolivroprofessor.helper_classes.PermissionsUtil;
import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static br.com.grupoccaa.projetolivroprofessor.activities.ReaderActivity.MESSAGE_PROGRESS;

public class EstanteActivity
        extends android.support.v7.app.AppCompatActivity {

    private RecyclerView recyclerView;
    private EstanteAdapter adapter;
    private List<Publicacao> listPublicacoes;
    private ImageView backdropImage;

    private static final int REQUEST_WRITE = 0;
    private static String PERMISSION_WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private BroadcastReceiver broadcastReceiver;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estante);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        listPublicacoes = new ArrayList<>();

        loadPublicacoes();

        RestClient.initialize();

        //Todo load das capas dos pdfs
//        try {
//            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void montaGrid(List<Publicacao> publicacoes) {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestWritePermission();
        } else {
            adapter = new EstanteAdapter(this,publicacoes);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(30), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

    private void requestWritePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Snackbar.make(this.getWindow().getDecorView().findViewById(R.id.main_content), R.string.permission_contacts_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.Ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(EstanteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE);
        }
    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    //Todo passar como parametro o usuario para que o sistema saiba quais sao as publicacoes liberadas para aquele usuario
    private void loadPublicacoes(){

        //Carrega a List<Publicacao> retrofit - getPublicacoes

        RestClient.getInstance().getPublicacoes(new Callback<List<Publicacao>>() {
            @Override
            public void onResponse(Call<List<Publicacao>> call, Response<List<Publicacao>> response) {

                listPublicacoes = response.body();
                montaGrid(listPublicacoes);

            }

            @Override
            public void onFailure(Call<List<Publicacao>> call, Throwable t) {
                Log.i("","");
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Snackbar.make(this.getWindow().getDecorView().findViewById(R.id.main_content), R.string.permission_granted,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(this.getWindow().getDecorView().findViewById(R.id.main_content), R.string.permission_not_granted,
                        Snackbar.LENGTH_SHORT).show();
            }
        }  else if (requestCode == REQUEST_WRITE) {
            if (PermissionsUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(this.getWindow().getDecorView().findViewById(R.id.main_content), R.string.permision_available_write,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(this.getWindow().getDecorView().findViewById(R.id.main_content), R.string.permission_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
