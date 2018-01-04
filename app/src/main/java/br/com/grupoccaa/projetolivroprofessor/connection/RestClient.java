package br.com.grupoccaa.projetolivroprofessor.connection;

import java.util.concurrent.TimeUnit;
import br.com.grupoccaa.projetolivroprofessor.interfaces.MyApiInterface;
import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by gerso on 1/3/2018.
 */

public class RestClient {
    private static final String BASE_URL = "http://lpwebapi20180103064413.azurewebsites.net/";
    private static final int TIMEOUT = 60000;

    private static RestClient instance;
    private Retrofit retrofit;
    private MyApiInterface apiService;

    public static void initialize(){
        if (instance == null){
            instance = new RestClient();
        }
    }

    public static RestClient getInstance(){
        initialize();
        return instance;
    }

    private RestClient(){
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getPublicacoes(Callback<Publicacao> callback){
        Call<Publicacao> call = apiService.getPublicacoes();
        call.enqueue(callback);
    }
}
