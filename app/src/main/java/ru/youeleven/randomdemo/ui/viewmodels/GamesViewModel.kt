package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.repository.Repository
import ru.youeleven.randomdemo.utils.Event
import ru.youeleven.randomdemo.utils.SortingRequest
import ru.youeleven.randomdemo.utils.SearchRequest
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class GamesViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _searchText = MutableStateFlow<SearchRequest?>(null)
    val searchText: StateFlow<SearchRequest?> = _searchText.asStateFlow()

    private val _games = MutableStateFlow<Flow<PagingData<Game>>?>(getGames(null, null))
    val games: StateFlow<Flow<PagingData<Game>>?> = _games.asStateFlow()

    private val currentSort = MutableStateFlow(SortingRequest.BY_DEFAULT)

    private val _sortInEdit = MutableStateFlow(SortingRequest.BY_DEFAULT)
    val sortInEdit: StateFlow<SortingRequest> = _sortInEdit

    private val _errorText = MutableStateFlow<Event<String?>>(Event(null))
    val errorText: StateFlow<Event<String?>> = _errorText


    private fun getGames(search: String?, sort: String?): Flow<PagingData<Game>> {
        return repository.getGamesPaginated(search, sort)
    }

    init {
        _searchText.debounce{ searchRequest ->
            searchRequest?.delayTime ?: 1000L
        }.onEach {
            if (it == null) return@onEach
            updateData(it.search, currentSort.value)
        }.launchIn(viewModelScope)
    }

    fun onSearchEdit(search: String) {
        _searchText.update { SearchRequest(search, 1000L) }
    }

    fun onSearchSubmit(search: String) {
        _searchText.update { SearchRequest(search, 0L) }
    }

    fun onSortEdit(request: SortingRequest) {
        _sortInEdit.update { request }
    }

    fun onSortSubmit() {
        if (_sortInEdit.value == currentSort.value) return
        updateData(_searchText.value?.search, _sortInEdit.value)
        currentSort.update { _sortInEdit.value }
    }

    private fun updateData(search: String?, sort: SortingRequest?) {
        _games.update {
            getGames(
                search = if (search.isNullOrBlank()) null else search,
                sort = if (sort == SortingRequest.BY_DEFAULT) null else sort?.request
            )
        }
    }
}