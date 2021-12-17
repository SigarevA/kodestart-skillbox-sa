package ru.sigarev.products.di

import ru.kode.products.domain.fakeusecase.FakeProductsUseCase
import ru.kode.products.domain.fakeusecase.ProductsUseCase
import toothpick.config.Module

class ProductsFakeDomainModule : Module() {
  init {
    bind(ProductsUseCase::class.java)
      .to(FakeProductsUseCase::class.java)
      .singletonInScope()
  }
}