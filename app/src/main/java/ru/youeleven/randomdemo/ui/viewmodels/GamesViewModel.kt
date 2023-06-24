package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _searchQueue = MutableStateFlow<String?>(null)
    val searchQueue: StateFlow<String?> = _searchQueue.asStateFlow()


    fun getGames(search: String?, sort: String?): Flow<PagingData<Game>> {
        return if (search.isNullOrBlank() && sort.isNullOrBlank()) {
            repository.getGamesPaginatedLocal().flow.cachedIn(viewModelScope)
                .map { pagingData -> pagingData.map { it.game.asGame() } }
        } else {
            repository.getGamesPaginatedRemote(search, sort).flow.cachedIn(viewModelScope)
                .map { pagingData -> pagingData.map { it } }
        }
    }

    fun updateSearch(searchString: String) {
        _searchQueue.update { searchString }
    }
}