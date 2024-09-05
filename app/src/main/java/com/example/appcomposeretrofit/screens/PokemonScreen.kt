
package com.example.appcomposeretrofit.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.appcomposeretrofit.R
import com.example.appcomposeretrofit.data.api.ApiProvider
import com.example.appcomposeretrofit.model.Pokemon
import com.example.appcomposeretrofit.data.repository.PokemonRepository
import com.example.appcomposeretrofit.model.PokemonDetail
import com.example.appcomposeretrofit.viewmodel.PokemonViewModel
import com.example.appcomposeretrofit.viewmodel.PokemonViewModelFactory

@Composable
fun PokemonScreen(viewModel: PokemonViewModel = viewModel()) {
    // Obtener la lista de Pokémon y el detalle del Pokémon seleccionado del ViewModel
    val pokemonList by viewModel.pokemonList.collectAsState()
    val selectedPokemonDetail by viewModel.pokemonDetail.collectAsState()

    // Debugging logs
    Log.d("PokemonScreen", "PokemonList: $pokemonList")
    Log.d("PokemonScreen", "SelectedPokemonDetail: $selectedPokemonDetail")

    // Cargar la lista de Pokémon al iniciar la pantalla
    LaunchedEffect(Unit) {
        try {
            viewModel.getPokemonList() // Cargar lista inicial
        } catch (e: Exception) {
            Log.e("PokemonScreen", "Error fetching Pokemon list", e)
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Master: Lista de Pokémon
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(pokemonList) { pokemon ->
                PokemonListItem(pokemon) {
                    viewModel.getPokemonDetail(pokemon.id) // Obtener detalles al hacer clic
                }
            }
        }

        // Detail: Detalle del Pokémon seleccionado
        selectedPokemonDetail?.let { detail ->
            PokemonDetailView(pokemonDetail = detail, modifier = Modifier.weight(1f).fillMaxHeight())
        }
    }
}

@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del Pokémon (Circular) o iniciales
        if (pokemon.url.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.url)
                    .transformations(CircleCropTransformation())
                    .build(),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder), // Placeholder si no carga
                error = painterResource(R.drawable.error_image) // Imagen de error
            )
        } else {
            // Mostrar iniciales si no hay URL
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pokemon.name.take(2).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Nombre del Pokémon
        Text(
            text = pokemon.name.replaceFirstChar { it.uppercaseChar() },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun PokemonDetailView(pokemonDetail: PokemonDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Sprite frontal del Pokémon
        AsyncImage(
            model = pokemonDetail.sprites.front_default,
            contentDescription = pokemonDetail.name,
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder), // Placeholder si no carga
            error = painterResource(R.drawable.error_image) // Imagen de error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre del Pokémon
        Text(
            text = pokemonDetail.name.replaceFirstChar { it.uppercaseChar() },
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Detalles adicionales: Altura, Peso, Tipos de Pokémon
        Text(text = "Altura: ${pokemonDetail.height} decímetros", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Peso: ${pokemonDetail.weight} hectogramos", style = MaterialTheme.typography.bodyLarge)

        // Tipos de Pokémon
        Text(text = "Tipos:", style = MaterialTheme.typography.labelSmall)
        Row {
            pokemonDetail.types.forEach { typeSlot ->
                Text(
                    text = typeSlot.type.name.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(4.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color.White
                )
            }
        }
    }
}
