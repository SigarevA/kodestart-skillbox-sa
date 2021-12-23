package ru.kode.base.intership.data.di

import ru.kode.base.intership.products.data.ProductsDataBase
import rukodebaseintershipproductsdata.CardEntityQueries
import javax.inject.Inject
import javax.inject.Provider

class CardQueriesProvider @Inject constructor(
  private val db: ProductsDataBase,
) : Provider<CardEntityQueries> {
  override fun get(): CardEntityQueries = db.cardEntityQueries
}