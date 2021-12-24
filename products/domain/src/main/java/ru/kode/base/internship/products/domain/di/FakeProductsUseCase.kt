package ru.kode.base.internship.products.domain.di

import ru.kode.base.internship.products.domain.fakeusecase.ProductsUseCase
import ru.kode.base.internship.products.domain.fakeusecase.ProductsUseCaseImpl
import toothpick.config.Module

class ProductsFakeDomainModule : Module() {
  init {
    bind(ProductsUseCase::class.java)
      .to(ProductsUseCaseImpl::class.java)
      .singletonInScope()
  }
}