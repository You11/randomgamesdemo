package ru.youeleven.randomdemo.utils

import ru.youeleven.randomdemo.App
import ru.youeleven.randomdemo.R
import java.io.IOException

class CallResult<T> {

    constructor(data: T) {
        _data = data
    }

    constructor(message: Exception = IOException(DEFAULT_ERROR_MESSAGE)) {
        _error = message
    }

    private var _data: T? = null
    private var _error: Exception? = null

    val isSuccess
        get() = _error == null
    val data
        get() = _data!!
    val error
        get() = _error
    val errorMessage
        get() = _error?.message ?: ""

    companion object {
        val DEFAULT_ERROR_MESSAGE: String = App.instance.getString(R.string.default_error)
    }
}