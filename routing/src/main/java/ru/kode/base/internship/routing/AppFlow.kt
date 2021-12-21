package ru.kode.base.internship.routing

import ru.kode.base.core.routing.coordinator.FlowConfig
import ru.kode.base.core.routing.coordinator.FlowConstructor
import ru.kode.base.core.routing.coordinator.FlowCoordinator
import ru.kode.base.internship.auth.domain.di.AuthDataModule
import ru.kode.base.internship.auth.domain.di.AuthDomainModule
import ru.kode.base.internship.domain.card.di.DetailCardDomainModule
import ru.kode.base.internship.products.domain.di.ProductsFakeDomainModule
import ru.kode.base.intership.data.products.di.ProductsDataModule

interface AppFlow {
  companion object : FlowConstructor<Coordinator, Unit, Unit>(
    FlowConfig(
      flowId = "app_flow",
      flowModules = listOf(
        AppFlowModule(),
        AuthDataModule(),
        AuthDomainModule(),
        ProductsFakeDomainModule(),
        ProductsDataModule(),
        DetailCardDomainModule()
        ),
      coordinatorClass = Coordinator::class.java
    )
  )

  interface Coordinator : FlowCoordinator<Event, Unit>

  sealed class Event {
    object LoginRequested : Event()
    object EnterPasswordDismissed : Event()
    object UserLoggedIn : Event()
    class CardDetailRequest(val cardId: Long) : Event()
  }
}
