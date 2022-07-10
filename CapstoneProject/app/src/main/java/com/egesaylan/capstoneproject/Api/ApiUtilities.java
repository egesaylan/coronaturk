package com.egesaylan.capstoneproject.Api;

import com.egesaylan.capstoneproject.Models.Informations;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {

    public static Retrofit retrofit = null;

    public static JsonPlaceHolderApi getAPIInterface() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(JsonPlaceHolderApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit.create(JsonPlaceHolderApi.class);

    }
}
