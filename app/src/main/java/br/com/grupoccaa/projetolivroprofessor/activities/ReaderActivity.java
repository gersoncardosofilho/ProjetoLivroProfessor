package br.com.grupoccaa.projetolivroprofessor.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.grupoccaa.projetolivroprofessor.R;
import br.com.grupoccaa.projetolivroprofessor.helper_classes.DownloadService;
import br.com.grupoccaa.projetolivroprofessor.models.Download;
import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;

public class ReaderActivity extends AppCompatActivity {

    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;

    ProgressBar mProgressBar;
    TextView mProgressText;
    Button mDownloadButton;
    Publicacao publicacao;
    private String urlPdf;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        publicacao = new Publicacao();
        publicacao = getIntent().getParcelableExtra("publicacao");
        urlPdf = publicacao.getUrlPublicacao();
        filename = publicacao.getTituloPublicacao();

        mProgressBar = findViewById(R.id.progress);
        mProgressText = findViewById(R.id.progress_text);
        mDownloadButton = findViewById(R.id.btn_download);

        registerReceiver();

        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });
    }

    public void downloadFile(){

        if(checkPermission()){
            startDownload(urlPdf);
        } else {
            requestPermission();
        }
    }

    private void startDownload(String urlPdf){

        Intent intent = new Intent(this,DownloadService.class);
        intent.putExtra("urlPdf", urlPdf);
        intent.putExtra("filename", filename);
        startService(intent);

    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    mProgressText.setText("File Download Complete");

                } else {

                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload(urlPdf);
                } else {

                    Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }


}
