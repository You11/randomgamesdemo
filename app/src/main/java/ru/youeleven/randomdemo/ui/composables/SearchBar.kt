package ru.youeleven.randomdemo.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesSearchBar(modifier: Modifier = Modifier, search: String?, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {
    var isActive by remember { mutableStateOf(false) }

    SearchBar(
        query = if (search.isNullOrBlank() && !isActive) "Search..." else search ?: "",
        onQueryChange = { onQueryChange.invoke(it) },
        onSearch = { onSearch.invoke(it) },
        active = false,
        onActiveChange = {
            isActive = it
        },
        modifier = modifier
    ) {}
}