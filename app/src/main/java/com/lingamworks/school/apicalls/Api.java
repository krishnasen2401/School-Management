package com.lingamworks.school.apicalls;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.POST;

public interface Api {
    @Multipart
@POST("addstudent.php")
Call<ResponseBody> addstudent(
        @Part("name") RequestBody name,
        @Part("dob") RequestBody dob,
        @Part("gender") RequestBody gender,
        @Part("class") RequestBody enclass,
        @Part("parent1") RequestBody parent1,
        @Part("phone1") RequestBody phone1,
        @Part("parent2") RequestBody parent2,
        @Part("phone2") RequestBody phone2,
        @Part("email") RequestBody email,
        @Part("fees") RequestBody fees,
        @Part MultipartBody.Part file
);
}
