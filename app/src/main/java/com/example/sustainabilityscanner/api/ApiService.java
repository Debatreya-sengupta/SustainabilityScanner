package com.example.sustainabilityscanner.api;

import com.example.sustainabilityscanner.model.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("product/{barcode}.json")  // ✅ Ensure the correct API endpoint
    Call<ProductResponse> getProduct(@Path("barcode") String barcode);
}
