package ru.kode.base.intership.data.di

import com.squareup.sqldelight.db.SqlDriver
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.intership.products.data.ProductsDataBase
import javax.inject.Inject
import javax.inject.Provider

class ProductDataBaseProvider @Inject constructor(
  private val driver: SqlDriver,
) : Provider<ProductsDataBase> {
  override fun get(): ProductsDataBase = ProductsDataBase(driver)
}