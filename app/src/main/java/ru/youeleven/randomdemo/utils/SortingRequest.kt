package ru.youeleven.randomdemo.utils

enum class SortingRequest(val request: String) {
    BY_DEFAULT("null"),
    BY_RATING("-rating"),
    BY_METACRITIC("-metacritic"),
    BY_RELEASE_DATE("-released")
}