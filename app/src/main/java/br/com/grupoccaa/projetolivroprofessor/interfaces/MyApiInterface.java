package br.com.grupoccaa.projetolivroprofessor.interfaces;

import br.com.grupoccaa.projetolivroprofessor.models.Publicacao;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gerso on 1/3/2018.
 */

public interface MyApiInterface {
    @GET("api/Publicacoes")
    Call<Publicacao> getPublicacoes();
}
