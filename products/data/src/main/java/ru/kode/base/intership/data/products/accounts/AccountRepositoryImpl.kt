package ru.kode.base.intership.data.products.accounts

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.kode.base.internship.products.domain.models.GeneralAccount
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.intership.data.products.FakeData
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor() : AccountRepository {
  private val _accounts: MutableSharedFlow<List<GeneralAccount>> = MutableSharedFlow()

  override val accounts: Flow<List<GeneralAccount>>
    get() = _accounts.asSharedFlow()

  override suspend fun load() {
    delay(2_300)
    _accounts.emit(FakeData.accounts
      .map {
        it.copy(balance = it.balance * Math.random() + 10)
      }
    )
  }
}