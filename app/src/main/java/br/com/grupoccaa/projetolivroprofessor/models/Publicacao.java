package br.com.grupoccaa.projetolivroprofessor.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gerson Cardoso on 1/3/2018.
 */

public class Publicacao implements Parcelable {

    private int idPublicacao;
    private String tituloPublicacao;
    private String urlPublicacao;

    public int getIdPublicacao() {
        return idPublicacao;
    }

    public void setIdPublicacao(int idPublicacao) {
        this.idPublicacao = idPublicacao;
    }

    public String getTituloPublicacao() {
        return tituloPublicacao;
    }

    public void setTituloPublicacao(String tituloPublicacao) {
        tituloPublicacao = tituloPublicacao;
    }

    public String getUrlPublicacao() {
        return urlPublicacao;
    }

    public void setUrlPublicacao(String urlPublicacao) {
        urlPublicacao = urlPublicacao;
    }

    public Publicacao(int idPublicacao, String tituloPublicacao, String urlPublicacao) {
        this.idPublicacao = idPublicacao;
        this.tituloPublicacao = tituloPublicacao;
        this.urlPublicacao = urlPublicacao;
    }

    //Parcelling

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idPublicacao);
        parcel.writeString(tituloPublicacao);
        parcel.writeString(urlPublicacao);
    }

    //Constructor for createFromParcel
    public Publicacao(Parcel in){
        this.idPublicacao = in.readInt();
        this.tituloPublicacao = in.readString();
        this.urlPublicacao = in.readString();
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
