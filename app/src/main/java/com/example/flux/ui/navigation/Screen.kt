package com.example.flux.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Discover : Screen("discover", "Discover")
    object Search : Screen("search", "Search")
    object Lists : Screen("lists", "My Lists")
    object Details : Screen("details/{type}/{id}", "Details") {
        fun createRoute(type: String, id: Int) = "details/$type/$id"
    }
}
