package com.example.retromeme

import retrofit2.Call
import retrofit2.http.GET

interface getdata {
    @GET("gimme")
fun get(): Call<DataItem>
}