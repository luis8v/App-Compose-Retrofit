package com.example.appcomposeretrofit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.appcomposeretrofit.data.repository.PokemonDetailEntity
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemons WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonDetailEntity)
}
