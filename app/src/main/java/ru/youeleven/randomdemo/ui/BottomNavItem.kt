package ru.youeleven.randomdemo.ui

import ru.youeleven.randomdemo.App
import ru.youeleven.randomdemo.R

sealed class BottomNavItem(val title: String, val icon: Int?, val screenRoute: String) {

    object Games : BottomNavItem(App.instance.getString(R.string.games_screen_name), R.drawable.ic_list,"games")
    object Favorites: BottomNavItem(App.instance.getString(R.string.favorite_screen_name), R.drawable.ic_favorites,"favorites")
    object Settings: BottomNavItem(App.instance.getString(R.string.settings_screen_name), R.drawable.ic_settings,"settings")
    object GameInfo: BottomNavItem(App.instance.getString(R.string.game_info_screen_name), null, "game_info") {
        val argName: String = "gameId"
    }

    fun getScreenRouteNameWithArgsGeneric(args: String) = "$screenRoute/{$args}"

    fun getScreenRouteNameWithArgs(args: String) = "$screenRoute/$args"
}