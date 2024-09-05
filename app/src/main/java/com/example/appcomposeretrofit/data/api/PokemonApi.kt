package com.example.appcomposeretrofit.api

import com.example.appcomposeretrofit.model.ListaPokemon
import com.example.appcomposeretrofit.model.PokemonDetail
import com.example.appcomposeretrofit.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//WEB SERVICE
// PokemonApi.kt
interface PokemonApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0
    ): ListaPokemon

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetail
}

//CLIENT
object ApiProvider {
    fun provideApi(): PokemonApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }
}