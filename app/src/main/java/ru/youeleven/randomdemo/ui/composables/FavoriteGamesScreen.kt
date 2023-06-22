package ru.youeleven.randomdemo.ui.composables

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.viewmodels.FavoriteGamesViewModel

@Composable
fun FavoriteGamesScreen(viewModel: FavoriteGamesViewModel, onGameInfoClick: (Int) -> Unit) {

    val games: List<Game> by viewModel.games.collectAsStateWithLifecycle()
    viewModel.getGames()
    Layout(games = games, onGameInfoClick = onGameInfoClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout(games: List<Game>, onGameInfoClick: (Int) -> Unit) {

    LazyColumn {
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
                        maxLines = 2
                    )
                    Text(text = "${it.rating ?: ""} | ${it.ratingCount}", modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                }
            }
        }
    }
}