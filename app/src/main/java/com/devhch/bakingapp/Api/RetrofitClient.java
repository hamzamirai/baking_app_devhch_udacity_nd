package com.devhch.bakingapp.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.devhch.bakingapp.Utils.Constant.BAKING_BASE_URL;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/26/2020.
 */
public class RetrofitClient {

    /**
     * Static variable for Retrofit
     */
    private static Retrofit sRetrofit = null;

    public static Retrofit getClient() {
        if (sRetrofit == null) {

            // Create the Retrofit instance using the builder
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BAKING_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
