package ru.youeleven.randomdemo.ui.composables.screens

import android.content.Context
import android.os.Build
import android.text.Html
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.youeleven.randomdemo.R
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.composables.ExpandingText
import ru.youeleven.randomdemo.ui.composables.Rating
import ru.youeleven.randomdemo.ui.showErrorIfExists
import ru.youeleven.randomdemo.ui.viewmodels.GameInfoViewModel
import ru.youeleven.randomdemo.utils.Event
import java.text.DateFormat

@Composable
fun GameInfoScreen(id: String?, viewModel: GameInfoViewModel) {

    viewModel.getGame(id)
    val game: Game? by viewModel.game.collectAsStateWithLifecycle()
    val isFavorite: Boolean by viewModel.isFavorite.collectAsStateWithLifecycle()
    val errorEvent by viewModel.errorText.collectAsStateWithLifecycle()
    showErrorIfExists(errorEvent, LocalContext.current)


    GameLayout(
        game,
        isFavorite,
        { viewModel.changeFavoriteGameStatus(true) },
        { viewModel.changeFavoriteGameStatus(false) }
    )
}

@Composable
fun GameLayout(game: Game?, isFavorite: Boolean, onAddClick: () -> Unit, onRemoveClick: () -> Unit) {
    if (game == null) return
    var showPager by rememberSaveable { mutableStateOf<Int?>(null) }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(enabled = true, state = scrollState)) {

        AsyncImage(
            model = game.backgroundImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Crop
        )

        Text(text = game.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            fontWeight = FontWeight(weight = 600),
            fontSize = 24.sp
        )

        val date = game.released?.let { DateFormat.getDateInstance(DateFormat.MEDIUM).format(it) }
        if (date != null) Text(text = stringResource(id = R.string.game_info_release_date, date), modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp))

        if (game.screenshots.isNotEmpty()) {
            ScreenshotsRow(screenshots = game.screenshots) {
                showPager = it
            }
        }

        if (game.description != null) {
            val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(game.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(game.description).toString()
            }
            ExpandingText(text = text, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp))
        }

        if (game.rating != null && game.ratingCount != null) {
            Rating(rating = game.rating, numberOfRatings = game.ratingCount, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
        }

        if (!isFavorite) {
            GameInfoGameStatusButton(onClick = { onAddClick.invoke() }, text = stringResource(id = R.string.game_info_add_game))
        } else {
            GameInfoGameStatusButton(onClick = { onRemoveClick.invoke() }, text = stringResource(id = R.string.game_info_remove_game))
        }

        showPager?.let {
            DialogueScreenshots(game.screenshots, it) {
                showPager = null
            }
        }
    }
}

@Composable
fun ScreenshotsRow(screenshots: List<String>, onImageClick: (Int) -> Unit) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .padding(horizontal = 8.dp)
        .horizontalScroll(state = scrollState)) {

        screenshots.forEachIndexed { index, url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(320.dp)
                    .clickable { onImageClick.invoke(index) }
            )
            if (index != screenshots.size - 1)
                Divider(
                    thickness = 3.dp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(3.dp)
                        .background(Color.Black)
                )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogueScreenshots(screenshots: List<String>, index: Int, onDismiss: () -> Unit) {
    val pagerState = rememberPagerState(index)
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)
    ) {
        HorizontalPager(state = pagerState, pageCount = screenshots.size, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) { page ->
            AsyncImage(model = screenshots[page], contentDescription = null)
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
            .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)) {
        Text(text = text, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}