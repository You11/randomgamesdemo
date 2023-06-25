package ru.youeleven.randomdemo.ui.composables

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Locale


@Composable
fun SettingsScreen(onThemeChange: (Boolean) -> Unit) {
    var isDarkTheme: Boolean by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxSize()) {
        Text(text = "Dark mode: ")
        Switch(checked = isDarkTheme, onCheckedChange = {
            onThemeChange(!it)
            isDarkTheme = !isDarkTheme
        })
    }
}