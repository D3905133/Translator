package uk.ac.tees.mad.tt.navigation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.tt.TranlatorViewmodel
import uk.ac.tees.mad.tt.screen.Home
import uk.ac.tees.mad.tt.screen.Login
import uk.ac.tees.mad.tt.screen.Result
import uk.ac.tees.mad.tt.screen.Signup
import uk.ac.tees.mad.tt.screen.Splash

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: TranlatorViewmodel = viewModel()
    Surface {
        NavHost(navController = navController, startDestination = AppNavComp.Splash.destination) {
            composable(AppNavComp.Splash.destination) {
                Splash(navController, viewModel)
            }
            composable(AppNavComp.Login.destination) {
                Login(navController, viewModel)
            }
            composable(AppNavComp.Signup.destination) {
                Signup(navController, viewModel)
            }
            composable(AppNavComp.Home.destination) {
                Home(navController, viewModel)
            }
            composable(
                route = AppNavComp.Result.destination,
                arguments = listOf(
                    navArgument("from") { type = NavType.StringType },
                    navArgument("result") { type = NavType.StringType },
                    navArgument("fromLang") { type = NavType.StringType },
                    navArgument("toLang") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val from = backStackEntry.arguments?.getString("from") ?: ""
                val result = backStackEntry.arguments?.getString("result") ?: ""
                val fromLang = backStackEntry.arguments?.getString("fromLang") ?: ""
                val toLang = backStackEntry.arguments?.getString("toLang") ?: ""

                Result(navController, viewModel, from, result, fromLang, toLang)
            }
        }
    }
}