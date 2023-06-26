package ru.youeleven.randomdemo.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel: ViewModel() {

    private val _isDarkThemeOn = MutableStateFlow(true)
    val isDarkThemeOn: StateFlow<Boolean> = _isDarkThemeOn.asStateFlow()


    fun enableDarkTheme(enabled: Boolean) {
        _isDarkThemeOn.update { enabled }
    }
}