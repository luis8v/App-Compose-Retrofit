package com.example.appcomposeretrofit.repository

import com.example.appcomposeretrofit.data.api.PokemonApi
import com.example.appcomposeretrofit.data.dao.PokemonDao
import com.example.appcomposeretrofit.model.ListaPokemon
import com.example.appcomposeretrofit.model.PokemonDetail
import com.example.appcomposeretrofit.model.PokemonType
import com.example.appcomposeretrofit.model.PokemonTypeSlot
import com.example.appcomposeretrofit.model.Sprites

/*class PokemonRepository(
    private val pokemonApi: PokemonApi
) {
    suspend fun getPokemonList(): ListaPokemon {
        return pokemonApi.getPokemonList()
    }
}
*/
class PokemonRepository(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao
) {

    suspend fun getPokemonList(): ListaPokemon {
        return pokemonApi.getPokemonList()
    }

    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        // Verificar si los datos están en la base de datos local
        val cachedPokemon = pokemonDao.getPokemonById(id)
        if (cachedPokemon != null) {
            return cachedPokemon.toPokemonDetail() // Convertimos la entidad de la base de datos al modelo de dominio
        }

        // Si no están en la base de datos, obtenerlos de la API
        val pokemonDetail = pokemonApi.getPokemonDetail(id)

        // Almacenar los datos en la base de datos local
        pokemonDao.insertPokemon(pokemonDetail.toEntity())

        return pokemonDetail
    }

    // Convertir el modelo de dominio a la entidad de Room
    fun PokemonDetail.toEntity(): PokemonDetailEntity {
        return PokemonDetailEntity(
            id = this.id,
            name = this.name,
            height = this.height,
            weight = this.weight,
            frontSprite = this.sprites.front_default,
            types = this.types.joinToString(",") { it.type.name } // Serialización simple
        )
    }

    // Convertir la entidad de Room al modelo de dominio
    fun PokemonDetailEntity.toPokemonDetail(): PokemonDetail {
        return PokemonDetail(
            id = this.id,
            name = this.name,
            height = this.height,
            weight = this.weight,
            sprites = Sprites(this.frontSprite),
            types = this.types.split(",").map { PokemonTypeSlot(0, PokemonType(it)) }
        )
    }

}