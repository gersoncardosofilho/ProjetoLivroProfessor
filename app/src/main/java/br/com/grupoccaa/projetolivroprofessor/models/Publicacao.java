package br.com.grupoccaa.projetolivroprofessor.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Gerson Cardoso on 1/3/2018.
 */

public class Publicacao implements Parcelable {

    private int IdPublicacao;
    private String TituloPublicacao;
    private String UrlPublicacao;
    private String UrlThumbnail;

    public String getUrlThumbnail() {
        return UrlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        UrlThumbnail = urlThumbnail;
    }

    public int getIdPublicacao() {
        return IdPublicacao;
    }

    public void setIdPublicacao(int idPublicacao) {
        this.IdPublicacao = idPublicacao;
    }

    public String getTituloPublicacao() {
        return TituloPublicacao;
    }

    public void setTituloPublicacao(String tituloPublicacao) {
        tituloPublicacao = tituloPublicacao;
    }

    public String getUrlPublicacao() {
        return UrlPublicacao;
    }

    public void setUrlPublicacao(String urlPublicacao) {
        urlPublicacao = urlPublicacao;
    }

    public Publicacao(int idPublicacao, String tituloPublicacao, String urlPublicacao, String urlThumbnail) {
        this.IdPublicacao = idPublicacao;
        this.TituloPublicacao = tituloPublicacao;
        this.UrlPublicacao = urlPublicacao;
        this.UrlThumbnail = urlThumbnail;
    }

    //Parcelling

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IdPublicacao);
        parcel.writeString(TituloPublicacao);
        parcel.writeString(UrlPublicacao);
        parcel.writeString(UrlThumbnail);
    }

    //Constructor for createFromParcel
    public Publicacao(Parcel in){
        this.IdPublicacao = in.readInt();
        this.TituloPublicacao = in.readString();
        this.UrlPublicacao = in.readString();
        this.UrlThumbnail = in.readString();
    }

    public static final Parcelable.Creator<Publicacao> CREATOR = new Parcelable.Creator<Publicacao>(){
        @Override
        public Publicacao createFromParcel(Parcel parcel) {
            return new Publicacao(parcel);
        }

        @Override
        public Publicacao[] newArray(int i) {
            return new Publicacao[i];
        }
    };
}
