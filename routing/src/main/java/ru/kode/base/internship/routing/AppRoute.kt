package ru.kode.base.internship.routing

import ru.kode.base.core.routing.Route

/**
 * Contains all application routes
 */
sealed class AppRoute(override val path: String) : Route {
  sealed class Login(path: String) : AppRoute(path) {
    object UserIdentificationKey : Login("/login/user_identification")
    object EnterPassword : Login("/login/password")
  }
  sealed class Products(path: String) : AppRoute(path) {
    object ProductsMainKey : Products("/login/user_identification")
  }
}
