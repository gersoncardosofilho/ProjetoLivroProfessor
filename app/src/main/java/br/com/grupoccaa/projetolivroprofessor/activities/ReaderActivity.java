package br.com.grupoccaa.projetolivroprofessor.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import br.com.grupoccaa.projetolivroprofessor.R;
import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;

public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null){
            Bundle bundle = savedInstanceState;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);



        Log.i("","");



    }
}
