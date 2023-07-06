package ru.youeleven.randomdemo.ui

import android.content.Context
import android.widget.Toast
import ru.youeleven.randomdemo.utils.Event

fun showErrorIfExists(errorEvent: Event<String?>, context: Context) {
    val error = errorEvent.getContentIfNotHandled()
    if (error != null) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}