package ru.youeleven.randomdemo.ui.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.composables.Rating
import ru.youeleven.randomdemo.ui.viewmodels.FavoriteGamesViewModel

@Composable
fun FavoriteGamesScreen(viewModel: FavoriteGamesViewModel, onGameInfoClick: (Int) -> Unit) {

    val games: List<Game> by viewModel.filtratedList.collectAsStateWithLifecycle()
    val search: String? by viewModel.searchQueue.collectAsStateWithLifecycle()

    Column {
        FavoriteGamesSearchBar(
            search = search,
            onQueryChange = {
                viewModel.onQueryChange(it)
            },
            onSearch = {
                viewModel.onSearch(it)
            }
        )
        GameLayout(games = games, onGameInfoClick = onGameInfoClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout(games: List<Game>, onGameInfoClick: (Int) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp)
    ) {
        items(games) {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(),
                onClick = { onGameInfoClick.invoke(it.id) }
            ) {
                Column {
                    AsyncImage(
                        model = it.backgroundImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(8.dp),
                        text = it.name,
                        maxLines = 2,
                        fontWeight = FontWeight(600)
                    )
                    if (it.rating != null && it.ratingCount != null) {
                        Rating(
                            rating = it.rating,
                            numberOfRatings = it.ratingCount,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteGamesSearchBar(search: String?, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {
    var isActive by remember { mutableStateOf(false) }
    SearchBar(
        query = if (search.isNullOrBlank() && !isActive) "Search..." else search ?: "",
        onQueryChange = { onQueryChange.invoke(it) },
        onSearch = { onSearch.invoke(it) },
        active = false,
        onActiveChange = {
            isActive = it
        },
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {}
}