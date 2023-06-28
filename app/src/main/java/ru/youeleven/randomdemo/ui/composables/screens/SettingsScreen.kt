package ru.youeleven.randomdemo.ui.composables.screens

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import ru.youeleven.randomdemo.R
import ru.youeleven.randomdemo.ui.viewmodels.SettingsViewModel
import java.util.Locale


@Composable
fun SettingsScreen(onThemeChange: (Boolean) -> Unit, viewModel: SettingsViewModel = viewModel()) {
    val isDarkTheme by viewModel.isDarkThemeOn.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.settings_dark_mode))
        Switch(checked = isDarkTheme, onCheckedChange = {
            onThemeChange(it)
            viewModel.enableDarkTheme(it)
        })
    }
}