package ru.youeleven.randomdemo.ui

import ru.youeleven.randomdemo.R

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String){

    object Games : BottomNavItem("Games", R.drawable.ic_list,"games")
    object Favorites: BottomNavItem("Favorites",R.drawable.ic_favorites,"favorites")
    object Settings: BottomNavItem("Settings",R.drawable.ic_settings,"settings")
}