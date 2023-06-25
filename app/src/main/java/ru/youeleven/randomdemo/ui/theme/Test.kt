package ru.youeleven.randomdemo.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun Meow() {
    var isDarkTheme by remember { mutableStateOf(true) }
}   