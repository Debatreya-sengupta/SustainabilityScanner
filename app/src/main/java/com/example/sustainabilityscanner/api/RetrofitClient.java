package com.example.sustainabilityscanner.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v0/"; // ✅ API Base URL

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // ✅ Setting the API Base URL
                    .addConverterFactory(GsonConverterFactory.create()) // ✅ Convert JSON to Java Object
                    .build();
        }
        return retrofit;
    }
}
