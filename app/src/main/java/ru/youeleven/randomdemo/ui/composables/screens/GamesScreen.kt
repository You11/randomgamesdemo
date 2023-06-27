package ru.youeleven.randomdemo.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.youeleven.randomdemo.R
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.ui.composables.GamesSearchBar
import ru.youeleven.randomdemo.ui.composables.Rating
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel
import ru.youeleven.randomdemo.utils.SortingRequest
import ru.youeleven.randomdemo.utils.SearchRequest

@Composable
fun GamesScreen(gameViewModel: GamesViewModel, onGameInfoClick: (Int) -> Unit) {

    val search: SearchRequest? by gameViewModel.searchText.collectAsStateWithLifecycle()
    val gamesFlow: Flow<PagingData<Game>>? by gameViewModel.games.collectAsStateWithLifecycle()
    val games = gamesFlow?.collectAsLazyPagingItems()
    val editingFilter by gameViewModel.sortInEdit.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            GamesSearchBar(
                search = search?.search,
                onQueryChange = {
                    gameViewModel.onSearchEdit(it)
                },
                onSearch = {
                    gameViewModel.onSearchSubmit(it)
                },
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 64.dp)
            )
            Icon(
                painterResource(id = R.drawable.ic_filter),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        showBottomSheet = true
                    }
            )
        }

        GamesLazyColumn(games, onGameInfoClick)

        if (showBottomSheet) {
            GamesModalBottomSheet(
                onDismiss = {
                    showBottomSheet = false
                },
                selectedFilter = editingFilter,
                onSelectFilterClick = {
                    gameViewModel.onSortEdit(it)
                },
                onFilterClick = {
                    gameViewModel.onSortSubmit()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesModalBottomSheet(
    onDismiss: () -> Unit,
    selectedFilter: SortingRequest,
    onSelectFilterClick: (SortingRequest) -> Unit,
    onFilterClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Filter")

            Text(text = "By default", modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectFilterClick.invoke(SortingRequest.BY_DEFAULT)
                }.background(if (selectedFilter == SortingRequest.BY_DEFAULT) Color.Magenta else Color.Transparent))
            Text(text = "By rating", modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectFilterClick.invoke(SortingRequest.BY_RATING)
                }.background(if (selectedFilter == SortingRequest.BY_RATING) Color.Magenta else Color.Transparent))
            Text(text = "By Metacritic", modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectFilterClick.invoke(SortingRequest.BY_METACRITIC)
                }.background(if (selectedFilter == SortingRequest.BY_METACRITIC) Color.Magenta else Color.Transparent))
            Text(text = "By release date", modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectFilterClick.invoke(SortingRequest.BY_RELEASE_DATE)
                }.background(if (selectedFilter == SortingRequest.BY_RELEASE_DATE) Color.Magenta else Color.Transparent))
        }

        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onFilterClick.invoke()
                    onDismiss.invoke()
                }
            }
        }) {
            Text("Accept")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesLazyColumn(games: LazyPagingItems<Game>?, onGameInfoClick: (Int) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = games?.itemCount ?: 0,
            key = games?.itemKey { it.id }
        ) { index ->
            val game = games?.get(index) ?: return@items

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
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
fun GamesFilterBottomSheet() {
    ModalBottomSheet(onDismissRequest = {}) {
        Column {
            Text(text = "meow")
            Text(text = "meow2")
        }
    }
}