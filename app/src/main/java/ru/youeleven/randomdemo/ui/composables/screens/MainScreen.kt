package ru.youeleven.randomdemo.ui.composables.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.youeleven.randomdemo.ui.BottomNavItem
import ru.youeleven.randomdemo.ui.MainViewModel
import ru.youeleven.randomdemo.ui.theme.RandomdemoTheme
import ru.youeleven.randomdemo.ui.viewmodels.FavoriteGamesViewModel
import ru.youeleven.randomdemo.ui.viewmodels.GameInfoViewModel
import ru.youeleven.randomdemo.ui.viewmodels.GamesViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    RandomdemoTheme(isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val controller = rememberNavController()

            Scaffold(
                bottomBar = {
                    if (isCurrentRouteWithBottomBar(controller.currentBackStackEntryAsState().value?.destination?.route))
                        BottomAppBar { BottomNavigationBar(navController = controller) }
                }
            ) { paddingValues ->
                NavHost(navController = controller, paddingValues) { isDarkTheme ->
                    viewModel.updateDarkTheme(isDarkTheme)
                }
            }
        }
    }
}

@Composable
fun NavHost(navController: NavHostController, paddingValues: PaddingValues, onThemeChange: (Boolean) -> Unit) {
    NavHost(navController, startDestination = BottomNavItem.Games.screenRoute, modifier = Modifier.padding(paddingValues)) {
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
            route = BottomNavItem.GameInfo.getScreenRouteNameWithArgsGeneric(BottomNavItem.GameInfo.argName),
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
            SettingsScreen(onThemeChange)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        routesWithBottomBar.forEach { item ->
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

private fun isCurrentRouteWithBottomBar(route: String?): Boolean {
    if (route == null) return false

    routesWithBottomBar.forEach {
        if (route.startsWith(it.screenRoute)) return true
    }

    return false
}

private val routesWithBottomBar = listOf(
    BottomNavItem.Games,
    BottomNavItem.Favorites,
    BottomNavItem.Settings
)