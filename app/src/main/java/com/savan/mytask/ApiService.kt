package com.savan.mytask

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    fun getPosts(): Call<List<Posts>>
}