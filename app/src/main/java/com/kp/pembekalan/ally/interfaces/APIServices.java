package com.kp.pembekalan.ally.interfaces;

import com.google.gson.annotations.SerializedName;
import com.kp.pembekalan.ally.models.ImageModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIServices {
    @Multipart
    @POST("/api/recommendation")
    Call<List<Recommendation>> uploadImage(@Part MultipartBody.Part photo,
                                           @PartMap Map<String, RequestBody> text);

    @Multipart
    @POST("/api/order")
    Call<List<Order>> orderProduct();/*@Part MultipartBody.Part catalog_id, @Part MultipartBody.Part user_id, @Part MultipartBody.Part quantity,
                                   @Part MultipartBody.Part total_amount,  @Part MultipartBody.Part corp_id,
                                   @Part MultipartBody.Part tenant_id,  @Part MultipartBody.Part device_id,
                                   @PartMap Map<Integer, RequestBody> order);*/
}

