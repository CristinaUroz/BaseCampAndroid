package com.basecamp.android.shared

class BaseCampException : Exception {

    private var code: Int? = null
    private var errorMessage: String? = null
    private var mapError: Map<String, List<String>> = HashMap()

    constructor(t: Throwable) : super(t)

    constructor(code: Int, errorMessage: String, mapError: Map<String, List<String>> = HashMap()) : super("$errorMessage ($code)") {
        this.code = code
        this.errorMessage = errorMessage
        this.mapError = mapError
    }

    fun getError(type: String): String? {
        val stringBuilder = StringBuilder()
        mapError[type]?.forEach { stringBuilder.append(it).append('\n') } ?: return null
        return stringBuilder.toString()
    }

    fun getCode(): Int? {
        return code
    }
}