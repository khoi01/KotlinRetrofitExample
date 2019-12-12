/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

//use the moshi builder to create moshi object with the KotlinJsonAdapterFactory
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

//Use retrofit builder with ScalarConverterFactory and BASE URL
private val retrofit = Retrofit.Builder()
        //.addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


//Implement the MarsAPIService interface with @Get properties returning a string
interface MarsAPIService{
    @GET("realestate")
    fun getProperties():
            //Call<String>
            Call<List<MarsProperty>>
}

// create the MarsAPI using retrofit to implement the MarsAPIService
object  MarsAPI{
    val retrofitService : MarsAPIService by lazy {
        retrofit.create(MarsAPIService::class.java)
    }
}