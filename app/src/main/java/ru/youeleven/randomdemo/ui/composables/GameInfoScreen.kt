package ru.youeleven.randomdemo.ui.composables

import android.text.Html
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.viewmodels.GameInfoViewModel
import java.text.DateFormat

@Composable
fun GameInfoScreen(id: String?, viewModel: GameInfoViewModel) {

    viewModel.getGame(id)
    val game: Game? by viewModel.game.collectAsStateWithLifecycle()
    game?.let { Layout(it) }
}

@Composable
fun Layout(game: Game) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(enabled = true, state = ScrollState(0))) {
        AsyncImage(
            model = game.backgroundImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = game.name, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))
        if (game.description != null) {
            Text(text = Html.fromHtml(game.description).toString(), modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
        }
        Text(text = "${game.rating ?: ""} | ${game.ratingCount}", modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))
        val date = game.released?.let { DateFormat.getDateInstance(DateFormat.MEDIUM).format(it) }
        if (date != null) Text(text = "Release date: $date", modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))
    }
}