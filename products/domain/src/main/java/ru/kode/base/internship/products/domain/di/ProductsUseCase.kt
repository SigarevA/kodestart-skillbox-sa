package ru.kode.base.internship.products.domain.di

import ru.kode.base.internship.products.domain.usecase.ProductsUseCase
import ru.kode.base.internship.products.domain.usecase.ProductsUseCaseImpl
import toothpick.config.Module

class ProductsDomainModule : Module() {
  init {
    bind(ProductsUseCase::class.java)
      .to(ProductsUseCaseImpl::class.java)
      .singletonInScope()
  }
}