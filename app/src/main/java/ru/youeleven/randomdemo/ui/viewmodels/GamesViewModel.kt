package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.repository.Repository

class GamesViewModel : ViewModel() {

    private val repository: Repository = Repository()

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games.asStateFlow()


    init {
        viewModelScope.launch {
            repository.getGames().flowOn(Dispatchers.IO).collect { games ->
                _games.update { games  }
            }
        }
    }

    fun getGames() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGames()
        }
    }
}