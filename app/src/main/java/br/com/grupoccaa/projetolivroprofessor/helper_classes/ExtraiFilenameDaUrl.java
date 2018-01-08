package br.com.grupoccaa.projetolivroprofessor.helper_classes;

import android.net.Uri;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;



/**
 * Created by gerso on 1/6/2018.
 */

public class ExtraiFilenameDaUrl {

    private String pattern = "/([\\w\\d_-]*)\\.?[^\\\\\\/]*$/i";

    public static String GetFilename(String urlString){

       Uri uri = Uri.parse(urlString);
       String result = FilenameUtils.getBaseName(uri.getPath());

       return result + ".PDF";
    }



}
