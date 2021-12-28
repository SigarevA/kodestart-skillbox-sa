package ru.kode.base.internship.products.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.models.GeneralAccount


interface AccountRepository {
  val accounts: Flow<List<GeneralAccount>>

  suspend fun load(isRefresh: Boolean)
}