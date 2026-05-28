package edu.itvo.roompersistence.presentation.navigation

sealed class StadiumRoutes(val route: String) {

    data object StadiumList : StadiumRoutes("stadium_list")

    data object AddStadium : StadiumRoutes("add_stadium")

    data object EditStadium : StadiumRoutes("edit_stadium/{stadiumId}") {

        fun createRoute(stadiumId: Long): String {
            return "edit_stadium/$stadiumId"
        }
    }
}
