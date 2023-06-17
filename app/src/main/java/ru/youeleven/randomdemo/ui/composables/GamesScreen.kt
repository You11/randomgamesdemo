package ru.youeleven.randomdemo.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.theme.RandomdemoTheme
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(gameViewModel: GamesViewModel = viewModel()) {
    val games: List<Game> by gameViewModel.games.collectAsStateWithLifecycle()
    gameViewModel.getGames()

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Games") }) },
        content = { Content(games) }
    )
}

@Composable
fun Content(games: List<Game>) {
    Column(content = {
        games.forEach { GameComposable(it.id.toString(), it.name) }
    })
}

@Composable
fun GameComposable(id: String, name: String) {
    Column {
        Text(text = id)
        Text(text = name)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomdemoTheme {
        Greeting()
    }
}