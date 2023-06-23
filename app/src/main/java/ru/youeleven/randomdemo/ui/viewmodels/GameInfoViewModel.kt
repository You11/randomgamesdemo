package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
class GameInfoViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _game = MutableStateFlow<Game?>(null)
    val game: StateFlow<Game?> = _game.asStateFlow()


    fun getGame(id: String?) {
        val intId = id?.toIntOrNull() ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = isFavoriteGame(intId)
            if (isFavorite) {
                _game.update { repository.getFavoriteGame(intId) }
            } else {
                val result = repository.getGameInfo(intId)
                if (result.isSuccess) {
                    _game.update { result.data }
                }
            }
        }
    }

    private fun isFavoriteGame(id: Int): Boolean {
        return repository.isFavoriteGame(id)
    }

    fun changeFavoriteGameStatus(isFavorite: Boolean) {
        val game = game.value ?: return
        _game.update { game.copy().also { it.isFavoriteGame = isFavorite } }
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                repository.insertFavoriteGame(game)
            } else {
                repository.deleteFavoriteGame(game.id)
            }
        }
    }
}