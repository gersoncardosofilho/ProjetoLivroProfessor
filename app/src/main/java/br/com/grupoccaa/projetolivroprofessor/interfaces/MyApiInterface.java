package br.com.grupoccaa.projetolivroprofessor.interfaces;

import java.util.List;

import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by gerso on 1/3/2018.
 */

public interface MyApiInterface {
    @GET("api/Publicacoes")
    Call<List<Publicacao>> getPublicacoes();

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


    @GET("PEC5_LP_COMPLETO.pdf")
    @Streaming
    Call<ResponseBody> downloadFile();

}
