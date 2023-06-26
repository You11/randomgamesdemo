package ru.youeleven.randomdemo.ui.composables.screens

import android.os.Build
import android.text.Html
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.composables.ExpandingText
import ru.youeleven.randomdemo.ui.composables.Rating
import ru.youeleven.randomdemo.ui.viewmodels.GameInfoViewModel
import java.text.DateFormat

@Composable
fun GameInfoScreen(id: String?, viewModel: GameInfoViewModel) {

    viewModel.getGame(id)
    val game: Game? by viewModel.game.collectAsStateWithLifecycle()

    GameLayout(
        game,
        { viewModel.changeFavoriteGameStatus(true) },
        { viewModel.changeFavoriteGameStatus(false) }
    )
}

@Composable
fun GameLayout(game: Game?, onAddClick: () -> Unit, onRemoveClick: () -> Unit) {
    if (game == null) return
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(enabled = true, state = ScrollState(0))) {

        AsyncImage(
            model = game.backgroundImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Crop
        )

        Text(text = game.name,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            fontWeight = FontWeight(weight = 600),
            fontSize = 18.sp
        )

        if (game.description != null) {
            val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(game.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(game.description).toString()
            }
            ExpandingText(text = text, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
        }

        if (game.rating != null && game.ratingCount != null) {
            Rating(rating = game.rating, numberOfRatings = game.ratingCount, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
        }

        val date = game.released?.let { DateFormat.getDateInstance(DateFormat.MEDIUM).format(it) }
        if (date != null) Text(text = "Дата выхода: $date", modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))

        if (!game.isFavoriteGame) {
            GameInfoGameStatusButton(onClick = { onAddClick.invoke() }, text = "Добавить")
        } else {
            GameInfoGameStatusButton(onClick = { onRemoveClick.invoke() }, text = "Удалить")
        }
    }
}

@Composable
fun GameInfoGameStatusButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = { onClick.invoke() },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp)) {
        Text(text = text, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}