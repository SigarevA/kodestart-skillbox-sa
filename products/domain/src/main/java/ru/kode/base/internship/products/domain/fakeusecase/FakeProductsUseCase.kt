package ru.kode.base.internship.products.domain.fakeusecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import javax.inject.Inject

class FakeProductsUseCase @Inject constructor(
  scope: CoroutineScope,
) : BaseUseCase<FakeProductsUseCase.State>(scope, State()), ProductsUseCase {

  data class State(
    val bankAccountState: LceState = LceState.None,
    val depositState: LceState = LceState.None,
    val deposits: List<Deposit> = emptyList(),
    val bankAccounts: List<BankAccount> = emptyList(),
  )

  override suspend fun loadBankAccount() {
    setState { copy(bankAccountState = LceState.Loading) }
    try {
      delay(3_300)
      setState {
        copy(
          bankAccountState = LceState.Content,
          bankAccounts = listOf(BankAccount())
        )
      }
    } catch (e: Exception) {
      setState { copy(bankAccountState = LceState.Error(e.message)) }
    }
  }

  override suspend fun loadDeposits() {
    setState { copy(depositState = LceState.Loading) }
    try {
      delay(5_300)
      setState {
        copy(
          depositState = LceState.Content,
          deposits = listOf(Deposit(), Deposit(), Deposit())
        )
      }
    } catch (e: Exception) {
      setState { copy(depositState = LceState.Error(e.message)) }
    }
  }

  override val accountState: Flow<LceState>
    get() = stateFlow.map { it.bankAccountState }.distinctUntilChanged()
  override val depositState: Flow<LceState>
    get() = stateFlow.map { it.depositState }.distinctUntilChanged()
  override val accountsFlow: Flow<List<BankAccount>>
    get() = stateFlow.map { it.bankAccounts }.distinctUntilChanged()
  override val depositsFlow: Flow<List<Deposit>>
    get() = stateFlow.map { it.deposits }.distinctUntilChanged()
}