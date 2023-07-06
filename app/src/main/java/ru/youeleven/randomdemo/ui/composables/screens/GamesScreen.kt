package ru.youeleven.randomdemo.ui.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import ru.youeleven.randomdemo.ui.showErrorIfExists
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel
import ru.youeleven.randomdemo.data.models.SortingRequest
import ru.youeleven.randomdemo.utils.SearchRequest

@Composable
fun GamesScreen(viewModel: GamesViewModel, onGameInfoClick: (Int) -> Unit) {

    val searchRequest: SearchRequest? by viewModel.searchText.collectAsStateWithLifecycle()
    val gamesFlow: Flow<PagingData<Game>>? by viewModel.games.collectAsStateWithLifecycle()
    val games = gamesFlow?.collectAsLazyPagingItems()
    val sortInEdit by viewModel.sortInEdit.collectAsStateWithLifecycle()
    val errorEvent by viewModel.errorText.collectAsStateWithLifecycle()
    showErrorIfExists(errorEvent, LocalContext.current)
    var showBottomSheet by remember { mutableStateOf(false) }

    if (games == null || games.itemCount == 0) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            GamesNotFound()
        }
    } else {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                GamesSearchBar(
                    search = searchRequest?.search,
                    onQueryChange = {
                        viewModel.onSearchEdit(it)
                    },
                    onSearch = {
                        viewModel.onSearchSubmit(it)
                    },
                    modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 64.dp)
                )

                SortIcon { showBottomSheet = true }
            }

            GamesLazyColumn(games, onGameInfoClick)

            if (showBottomSheet) {
                GamesModalBottomSheet(
                    onDismiss = {
                        showBottomSheet = false
                    },
                    selectedSort = sortInEdit,
                    onSelectSortClick = {
                        viewModel.onSortEdit(it)
                    },
                    onSortClick = {
                        viewModel.onSortSubmit()
                    }
                )
            }
        }
    }
}

@Composable
fun GamesNotFound() {
    Text(
        text = stringResource(id = R.string.no_games_found),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
    )
}

@Composable
fun SortIcon(onClick: () -> Unit) {
    Icon(
        painterResource(id = R.drawable.ic_filter),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick.invoke()
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesModalBottomSheet(
    onDismiss: () -> Unit,
    selectedSort: SortingRequest,
    onSelectSortClick: (SortingRequest) -> Unit,
    onSortClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState
    ) {
        Text(
            text = stringResource(id = R.string.games_bottom_sheet_title),
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
        )

        SortOption(
            text = stringResource(id = R.string.games_bottom_sheet_option_default),
            sortingRequest = SortingRequest.BY_DEFAULT,
            onSelectFilterClick = {onSelectSortClick.invoke(SortingRequest.BY_DEFAULT)},
            selectedFilter = selectedSort
        )
        SortOption(
            text = stringResource(id = R.string.games_bottom_sheet_option_rating),
            sortingRequest = SortingRequest.BY_RATING,
            onSelectFilterClick = {onSelectSortClick.invoke(SortingRequest.BY_RATING)},
            selectedFilter = selectedSort
        )
        SortOption(
            text = stringResource(id = R.string.games_bottom_sheet_option_metacritic),
            sortingRequest = SortingRequest.BY_METACRITIC,
            onSelectFilterClick = {onSelectSortClick.invoke(SortingRequest.BY_METACRITIC)},
            selectedFilter = selectedSort
        )
        SortOption(
            text = stringResource(id = R.string.games_bottom_sheet_option_release_date),
            sortingRequest = SortingRequest.BY_RELEASE_DATE,
            onSelectFilterClick = {onSelectSortClick.invoke(SortingRequest.BY_RELEASE_DATE)},
            selectedFilter = selectedSort
        )

        Button(modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .fillMaxWidth(), onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onSortClick.invoke()
                    onDismiss.invoke()
                }
            }
        }) {
            Text(stringResource(id = R.string.games_bottom_sheet_sort_button))
        }
    }
}

@Composable
fun SortOption(text: String,
               sortingRequest: SortingRequest,
               onSelectFilterClick: (SortingRequest) -> Unit,
               selectedFilter: SortingRequest
) {
    Text(text = text, modifier = Modifier
        .fillMaxWidth()
        .clickable { onSelectFilterClick.invoke(sortingRequest) }
        .background(if (selectedFilter == sortingRequest) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
        .padding(8.dp)
        .clip(RoundedCornerShape(8.dp))
    )
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
    }
}