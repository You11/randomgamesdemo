package ru.youeleven.randomdemo.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.youeleven.randomdemo.data.local.models.GameLocalWithRemoteKeys
import ru.youeleven.randomdemo.ui.theme.RandomdemoTheme
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(gameViewModel: GamesViewModel = viewModel()) {
    val games = gameViewModel.getGames().collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Games") }) },
        content = { Content(games) }
    )
}

@Composable
fun Content(games: LazyPagingItems<GameLocalWithRemoteKeys>) {
    LazyColumn {
        items(
            count = games.itemCount,
            key = games.itemKey { it.remoteKeys.id },
            contentType = games.itemContentType { "" }
        ) { index ->
            Text(modifier = Modifier.height(75.dp), text = games[index]?.game?.name ?: index.toString())

            Divider()
        }

        when (games.loadState.refresh) {
            is LoadState.Error -> {

            }
            is LoadState.Loading -> {
                item {
                    Column(modifier = Modifier.fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(modifier = Modifier.padding(8.dp), text = "Refresh Loading")

                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            }
            else -> {}
        }

        when (games.loadState.append) {
            is LoadState.Error -> {

            }
            is LoadState.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Pagination Loading")

                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomdemoTheme {
        Greeting()
    }
}