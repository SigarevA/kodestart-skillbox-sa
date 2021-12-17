package ru.kode.base.internship.products.domain.fakeusecase

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.core.domain.entity.LceState

interface ProductsUseCase {
  suspend fun loadBankAccount()
  suspend fun loadDeposits()

  val accountState: Flow<LceState>
  val depositState: Flow<LceState>

  val accountsFlow: Flow<List<BankAccount>>
  val depositsFlow: Flow<List<Deposit>>
}