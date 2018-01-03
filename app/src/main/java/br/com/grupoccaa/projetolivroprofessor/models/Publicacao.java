package br.com.grupoccaa.projetolivroprofessor.models;

/**
 * Created by gerso on 1/3/2018.
 */

public class Publicacao {
    private int idPublicacao;
    private String TituloPublicacao;
    private String PublicacaoUrl;


    public int getIdPublicacao() {
        return idPublicacao;
    }

    public void setIdPublicacao(int idPublicacao) {
        this.idPublicacao = idPublicacao;
    }

    public String getTituloPublicacao() {
        return TituloPublicacao;
    }

    public void setTituloPublicacao(String tituloPublicacao) {
        TituloPublicacao = tituloPublicacao;
    }

    public String getPublicacaoUrl() {
        return PublicacaoUrl;
    }

    public void setPublicacaoUrl(String publicacaoUrl) {
        PublicacaoUrl = publicacaoUrl;
    }
}
