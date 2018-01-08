package br.com.grupoccaa.projetolivroprofessor.helper_classes;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by gerso on 1/6/2018.
 */

public class VerificaArquivosNoDispositivo {

    public static final String TAG = VerificaArquivosNoDispositivo.class.getName();

    public static boolean IsAlreadyOnDevice(String mFilename){
        String path = null;
        boolean sdExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdExist) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/input_files/";
            File myFile = new File(path, mFilename);
            if (!myFile.exists()) {

            }
        }
        return true;
    }
}
