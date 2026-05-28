package edu.itvo.roompersistence.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.itvo.roompersistence.presentation.player.screen.AddPlayerScreen
import edu.itvo.roompersistence.presentation.player.screen.PlayerListScreen
import edu.itvo.roompersistence.presentation.stadium.screen.AddStadiumScreen
import edu.itvo.roompersistence.presentation.stadium.screen.StadiumListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PlayerRoutes.PlayerList.route
    ) {

        /*
        =========================================
        PLAYER LIST
        =========================================
         */

        composable(route = PlayerRoutes.PlayerList.route) {

            PlayerListScreen(

                onNavigateToAddPlayer = {
                    navController.navigate(PlayerRoutes.AddPlayer.route)
                },

                onNavigateToEditPlayer = { playerId ->
                    navController.navigate(
                        PlayerRoutes.EditPlayer.createRoute(playerId)
                    )
                },

                onNavigateToStadiums = {
                    navController.navigate(StadiumRoutes.StadiumList.route)
                }
            )
        }

        /*
        =========================================
        ADD PLAYER
        =========================================
         */

        composable(route = PlayerRoutes.AddPlayer.route) {

            AddPlayerScreen(
                playerId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        /*
        =========================================
        EDIT PLAYER
        =========================================
         */

        composable(
            route = PlayerRoutes.EditPlayer.route,
            arguments = listOf(navArgument("playerId") { type = NavType.LongType })
        ) { backStackEntry ->

            val playerId = backStackEntry.arguments?.getLong("playerId")

            AddPlayerScreen(
                playerId = playerId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        /*
        =========================================
        STADIUM LIST
        =========================================
         */

        composable(route = StadiumRoutes.StadiumList.route) {

            StadiumListScreen(

                onNavigateToAddStadium = {
                    navController.navigate(StadiumRoutes.AddStadium.route)
                },

                onNavigateToEditStadium = { stadiumId ->
                    navController.navigate(
                        StadiumRoutes.EditStadium.createRoute(stadiumId)
                    )
                }
            )
        }

        /*
        =========================================
        ADD STADIUM
        =========================================
         */

        composable(route = StadiumRoutes.AddStadium.route) {

            AddStadiumScreen(
                stadiumId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        /*
        =========================================
        EDIT STADIUM
        =========================================
         */

        composable(
            route = StadiumRoutes.EditStadium.route,
            arguments = listOf(navArgument("stadiumId") { type = NavType.LongType })
        ) { backStackEntry ->

            val stadiumId = backStackEntry.arguments?.getLong("stadiumId")

            AddStadiumScreen(
                stadiumId = stadiumId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}