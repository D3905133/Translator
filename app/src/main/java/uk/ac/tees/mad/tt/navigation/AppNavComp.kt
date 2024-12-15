package uk.ac.tees.mad.tt.navigation

sealed class AppNavComp(val destination : String) {
    object Splash : AppNavComp("splash")
    object Login : AppNavComp("login")
    object Signup : AppNavComp("signup")
    object Home : AppNavComp("home")
}