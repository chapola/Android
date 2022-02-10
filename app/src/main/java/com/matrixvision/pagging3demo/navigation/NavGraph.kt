package com.matrixvision.pagging3demo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import com.matrixvision.pagging3demo.screens.home.HomeScreen
import com.matrixvision.pagging3demo.screens.search.SearchScreen

@ExperimentalPagingApi
@Composable
fun SetupNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable( route = Screen.Home.route){
            HomeScreen(navController= navController)
        }

        composable(route = Screen.Search.route){
            SearchScreen(navController)
        }
    }
}