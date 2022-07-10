package com.egesaylan.capstoneproject.Api;

import com.egesaylan.capstoneproject.Models.Informations;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    //verileri çekeceğimiz internet sitesinin linkini base url ye atadık
    String BASE_URL ="https://corona.lmao.ninja/v2/";

    //get methodunu hangi html sayfasından yapacağımızın değerini girdik(countries) ve call methodunu oluşturduk
    @GET("countries")
    Call<List<Informations>> getInformations ();

}
