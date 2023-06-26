package ru.youeleven.randomdemo.ui.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.composables.Rating
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel

@Composable
fun GamesScreen(gameViewModel: GamesViewModel, onGameInfoClick: (Int) -> Unit) {

    val games = gameViewModel.games.collectAsLazyPagingItems()
    val search: String? by gameViewModel.searchQueue.collectAsStateWithLifecycle()

    Column {
        GamesSearchBar(
            search = search,
            onQueryChange = {
                gameViewModel.onQueryChange(it)
            },
            onSearch = {
                gameViewModel.onSearch(it)
            }
        )

        GamesLazyColumn(games, onGameInfoClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesLazyColumn(games: LazyPagingItems<Game>, onGameInfoClick: (Int) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = games.itemCount,
            key = games.itemKey { it.id }
        ) { index ->
            val game = games[index] ?: return@items

            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(),
                onClick = {
                    onGameInfoClick.invoke(game.id)
                }
            ) {
                Column {
                    AsyncImage(
                        model = game.backgroundImage,
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
                        text = game.name,
                        maxLines = 2,
                        fontWeight = FontWeight(600)
                    )
                    if (game.rating != null && game.ratingCount != null) {
                        Rating(
                            rating = game.rating,
                            numberOfRatings = game.ratingCount,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
        }

        //TODO: Loading indicator
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesSearchBar(search: String?, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {
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