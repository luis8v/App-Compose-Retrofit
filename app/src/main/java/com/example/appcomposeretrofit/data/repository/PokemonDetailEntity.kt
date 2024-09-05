package com.example.appcomposeretrofit.data.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val frontSprite: String,
    val types: String // Se almacenan los tipos como un String (se puede usar Gson para serializar y deserializar)
)
