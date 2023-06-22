package ru.youeleven.randomdemo.ui.viewmodels

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
    val games: StateFlow<List<Game>> = _games.asStateFlow()


    fun getGames() {
        viewModelScope.launch(Dispatchers.IO) {
            _games.update { repository.getFavoriteGames() }
        }
    }
}