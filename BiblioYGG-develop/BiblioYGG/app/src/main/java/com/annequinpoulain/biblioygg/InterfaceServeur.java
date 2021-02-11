package com.annequinpoulain.biblioygg;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterfaceServeur {
    @POST("livredata.php")
    @FormUrlEncoded
    Call<ResponseBody> livreDATA(@Field("request") String request, @Field("nom") String nom, @Field("prix") double prix, @Field("auteur") String auteur, @Field("cat") String cat, @Field("description") String description, @Field("stat") char stat, @Field("imgpath") String imgpath, @Field("email") String email, @Field("title") String title);

    @POST("getLivres.php")
    @FormUrlEncoded
    Call<List<LivreRetrofit>> getLivres(@Field("request") String request,  @Field("nom") String nom, @Field("email") String email);


    @POST("userdata.php")
    @FormUrlEncoded
    Call<ResponseBody> userDATA(@Field("email") String email, @Field("password") String password, @Field("nom") String nom, @Field("prenom") String prenom, @Field("tel") String tel);

    /*@POST("userExists.php")
    @FormUrlEncoded
    Call<JsonResponse> userExists(@Field("email") String email);

    @POST("getUserbyid.php")
    @FormUrlEncoded
    Call<Utilisateur> getUserbyid(@Field("id") int id);*/

    @POST("getUserlogin.php")
    @FormUrlEncoded
    Call<List<Utilisateur>> getUserlogin(@Field("email") String email, @Field("password") String password);

    @POST("getUsers.php")
    Call<List<Utilisateur>> getUsers();


    @Multipart
    @POST("uploadfile.php")
    Call<ResponseBody> upload(
            @Part("requete") RequestBody requete,
            @Part MultipartBody.Part image
    );

}
