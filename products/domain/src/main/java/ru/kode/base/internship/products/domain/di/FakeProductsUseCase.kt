package ru.kode.base.internship.products.di

import ru.kode.base.internship.products.domain.fakeusecase.FakeProductsUseCase
import ru.kode.base.internship.products.domain.fakeusecase.ProductsUseCase
import toothpick.config.Module

class ProductsFakeDomainModule : Module() {
  init {
    bind(ProductsUseCase::class.java)
      .to(FakeProductsUseCase::class.java)
      .singletonInScope()
  }
}