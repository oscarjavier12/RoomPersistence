package edu.itvo.roompersistence.presentation.navigation

sealed class PlayerRoutes(
    val route: String
) {

    data object PlayerList : PlayerRoutes(
        "player_list"
    )

    data object AddPlayer : PlayerRoutes(
        "add_player"
    )

    data object EditPlayer : PlayerRoutes(
        "edit_player/{playerId}"
    ) {

        fun createRoute(
            playerId: Long
        ): String {

            return "edit_player/$playerId"
        }
    }
}