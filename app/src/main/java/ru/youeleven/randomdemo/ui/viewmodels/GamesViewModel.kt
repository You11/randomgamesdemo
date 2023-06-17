package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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


    fun getGames() = repository.getGamesPaginated().flow.cachedIn(viewModelScope)
}