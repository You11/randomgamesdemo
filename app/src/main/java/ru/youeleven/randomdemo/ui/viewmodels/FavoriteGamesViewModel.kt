package ru.youeleven.randomdemo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class FavoriteGamesViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())

    private val _filtratedList = MutableStateFlow<List<Game>>(emptyList())
    val filtratedList: StateFlow<List<Game>> = _filtratedList

    private val _searchQueue = MutableStateFlow<String?>(null)
    val searchQueue: StateFlow<String?> = _searchQueue.asStateFlow()


    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.getFavoriteGames()
            _games.update { results }
            _filtratedList.update { results }
        }
    }

    fun onQueryChange(query: String) {
        _searchQueue.update { query }
    }

    fun onSearch(search: String) {
        _filtratedList.update {
            _games.value.filter { it.name.startsWith(search, ignoreCase = true) }
        }
    }
}