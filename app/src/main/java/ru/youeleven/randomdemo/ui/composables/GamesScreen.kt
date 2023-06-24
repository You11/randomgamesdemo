package ru.youeleven.randomdemo.ui.composables

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(gameViewModel: GamesViewModel, onGameInfoClick: (Int) -> Unit) {

    val games = gameViewModel.getGames(null, null).collectAsLazyPagingItems()

    LazyVerticalGrid(
        modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = games.itemCount,
            key = games.itemKey { it.id },
            contentType = games.itemContentType { "" }
        ) { index ->
            val game = games[index]

            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(),
                onClick = {
                    if (game?.id != null) onGameInfoClick.invoke(game.id)
                }
            ) {
                Column {
                    AsyncImage(
                        model = game?.backgroundImage,
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
                        text = game?.name ?: "",
                        maxLines = 2
                    )
                    Text(text = "${game?.rating ?: ""} | ${game?.ratingCount}", modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                }
            }
        }

        //TODO: Loading indicator
    }
}