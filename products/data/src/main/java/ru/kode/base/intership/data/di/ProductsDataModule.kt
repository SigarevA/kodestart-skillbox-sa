package ru.kode.base.intership.data.di

import ru.kode.base.internship.domain.card.repositories.DetailCardRepository
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.data.products.accounts.AccountRepositoryImpl
import ru.kode.base.intership.data.products.cards.CardRepositoryImpl
import ru.kode.base.intership.data.products.deposits.DepositRepositoryImpl
import ru.kode.base.intership.products.data.ProductsDataBase
import rukodebaseintershipproductsdata.CardEntityQueries
import toothpick.config.Module
import java.text.DateFormat
import java.text.SimpleDateFormat

class ProductsDataModule : Module() {
  init {
    bind(ProductsAPI::class.java)
      .toProvider(ProductsAPIProvider::class.java)
      .providesSingletonInScope()

    bind(AccountRepository::class.java)
      .to(AccountRepositoryImpl::class.java)
      .singletonInScope()

    bind(CardRepository::class.java)
      .to(CardRepositoryImpl::class.java)
      .singletonInScope()

    bind(DetailCardRepository::class.java)
      .to(CardRepositoryImpl::class.java)
      .singletonInScope()

    bind(DepositRepository::class.java)
      .to(DepositRepositoryImpl::class.java)
      .singletonInScope()

    bind(ProductsDataBase::class.java)
      .toProvider(ProductDataBaseProvider::class.java)
      .singletonInScope()

    bind(CardEntityQueries::class.java)
      .toProvider(CardQueriesProvider::class.java)
      .singletonInScope()

    bind(SimpleDateFormat::class.java)
      .withName(SIMPLE_DATE_TERM)
      .toProvider(TermDateProvider::class.java)
      .singletonInScope()

    bind(DateFormat::class.java)
      .withName(SIMPLE_DATE_CARD)
      .toProvider(CardDateProvider::class.java)
      .singletonInScope()
  }

  companion object {
    internal const val SIMPLE_DATE_TERM = "DateTermDeposit"
    internal const val SIMPLE_DATE_CARD = "DateCard"
  }
}