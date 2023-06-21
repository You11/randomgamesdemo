package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class GameInfoViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _game = MutableStateFlow<Game?>(null)
    val game: StateFlow<Game?> = _game.asStateFlow()


    suspend fun getGame(id: Int) {
        val result = repository.getGameInfo(id)
        if (result.isSuccess) {
            _game.update { result.data }
        }
    }
}