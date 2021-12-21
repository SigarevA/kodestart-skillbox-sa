package ru.kode.base.internship.routing

import ru.kode.base.core.model.ScreenKey
import ru.kode.base.internship.card.ui.CardDetailKey
import ru.kode.base.internship.products.ui.ProductsMainKey
import ru.kode.base.internship.products.ui.identification.UserIdentificationKey
import ru.kode.base.internship.products.ui.password.EnterPasswordKey

class ScreenKeyFactory : Function1<AppRoute, ScreenKey> {
  override fun invoke(route: AppRoute): ScreenKey {
    return when (route) {
      is AppRoute.Login.UserIdentificationKey -> UserIdentificationKey
      is AppRoute.Login.EnterPassword -> EnterPasswordKey
      is AppRoute.Products.Main -> ProductsMainKey
      is AppRoute.Products.DetailCard -> CardDetailKey(route.cardId)
    }
  }
}