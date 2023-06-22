package ru.youeleven.randomdemo.ui

import ru.youeleven.randomdemo.R

sealed class BottomNavItem(val title: String, val icon: Int?, val screenRoute: String) {

    object Games : BottomNavItem("Games", R.drawable.ic_list,"games")
    object Favorites: BottomNavItem("Favorites", R.drawable.ic_favorites,"favorites")
    object Settings: BottomNavItem("Settings", R.drawable.ic_settings,"settings")
    object GameInfo: BottomNavItem("Game Info", null, "game_info") {
        val argName: String = "gameId"
    }

    fun getScreenRouteNameWithArgsGeneric(args: String) = "$screenRoute/{$args}"

    fun getScreenRouteNameWithArgs(args: String) = "$screenRoute/$args"
}