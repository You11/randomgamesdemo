package ru.youeleven.randomdemo.ui.composables

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.youeleven.randomdemo.ui.BottomNavItem
import ru.youeleven.randomdemo.ui.viewmodels.FavoriteGamesViewModel
import ru.youeleven.randomdemo.ui.viewmodels.GameInfoViewModel
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val controller = rememberNavController()

    Scaffold(
        bottomBar = {
            if (isCurrentRouteWithBottomBar(controller.currentBackStackEntryAsState().value?.destination?.route))
                BottomAppBar() { BottomNavigationBar(navController = controller) }
        }
    ) {
        NavHost(navController = controller)
    }
}

fun isCurrentRouteWithBottomBar(route: String?): Boolean {
    if (route == null) return false

    val routesWithBottomBar = listOf(
        BottomNavItem.Games,
        BottomNavItem.Favorites,
        BottomNavItem.Settings
    )

    routesWithBottomBar.forEach {
        if (route.startsWith(it.screenRoute)) return true
    }

    return false
}

@Composable
fun NavHost(navController: NavHostController) {

    NavHost(navController, startDestination = BottomNavItem.Games.screenRoute) {
        composable(BottomNavItem.Games.screenRoute) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(BottomNavItem.Games.screenRoute)
            }
            val viewModel = hiltViewModel<GamesViewModel>(parentEntry)
            GamesScreen(viewModel) { id ->
                navController.navigate(BottomNavItem.GameInfo.getScreenRouteNameWithArgs(id.toString())) {
                    restoreState = true
                }
            }
        }
        composable(
            route = "game_info/{gameId}",
            arguments = listOf(navArgument(BottomNavItem.GameInfo.argName) { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<GameInfoViewModel>()
            GameInfoScreen(it.arguments?.getString(BottomNavItem.GameInfo.argName), viewModel)
        }
        composable(BottomNavItem.Favorites.screenRoute) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(BottomNavItem.Favorites.screenRoute)
            }
            val viewModel = hiltViewModel<FavoriteGamesViewModel>(parentEntry)
            FavoriteGamesScreen(viewModel) { id ->
                navController.navigate(BottomNavItem.GameInfo.getScreenRouteNameWithArgs(id.toString())) {
                    restoreState = true
                }
            }
        }
        composable(BottomNavItem.Settings.screenRoute) {
            SettingsScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Games,
        BottomNavItem.Favorites,
        BottomNavItem.Settings
    )

    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { if (item.icon != null) Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}