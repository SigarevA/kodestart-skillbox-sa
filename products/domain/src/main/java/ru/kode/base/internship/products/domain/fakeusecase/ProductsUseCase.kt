package ru.kode.base.internship.products.domain.fakeusecase

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.Deposit

interface ProductsUseCase {
  suspend fun loadBankAccount(isRefresh: Boolean)
  suspend fun loadDeposits(isRefresh: Boolean)

  val accountState: Flow<LceState>
  val depositState: Flow<LceState>

  val accountsFlow: Flow<List<Account>>
  val depositsFlow: Flow<List<Deposit>>
}