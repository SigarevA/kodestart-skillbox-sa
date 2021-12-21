package ru.kode.base.internship.products.domain.fakeusecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import javax.inject.Inject

internal class FakeProductsUseCase @Inject constructor(
  private val accountRepository: AccountRepository,
  private val depositRepository: DepositRepository,
  private val cardRepository: CardRepository,
  scope: CoroutineScope,
) : BaseUseCase<FakeProductsUseCase.State>(scope, State()), ProductsUseCase {

  data class State(
    val bankAccountState: LceState = LceState.None,
    val depositState: LceState = LceState.None,
  )

  override suspend fun loadBankAccount(isRefresh: Boolean) {
    if (!isRefresh)
      setState { copy(bankAccountState = LceState.Loading) }
    try {
      accountRepository.load()
      setState {
        copy(bankAccountState = LceState.Content)
      }
    } catch (e: Exception) {
      setState { copy(bankAccountState = LceState.Error(e.message)) }
    }
  }

  override suspend fun loadDeposits(isRefresh: Boolean) {
    if (!isRefresh)
      setState { copy(depositState = LceState.Loading) }
    try {
      depositRepository.load()
      setState {
        copy(depositState = LceState.Content)
      }
    } catch (e: Exception) {
      setState { copy(depositState = LceState.Error(e.message)) }
    }
  }

  override val accountState: Flow<LceState>
    get() = stateFlow.map { it.bankAccountState }.distinctUntilChanged()
  override val depositState: Flow<LceState>
    get() = stateFlow.map { it.depositState }.distinctUntilChanged()

  override val accountsFlow: Flow<List<Account>>
    get() = accountRepository.accounts.map { accounts ->
      accounts
        .map { account ->
          Account(
            account.id,
            account.number,
            account.balance,
            enumValueOf(account.currency),
            account.status,
            account.cards.asFlow()
              .map { cardId -> cardRepository.cardDetails(cardId).first() }
              .toList()
          )
        }
    }

  override val depositsFlow: Flow<List<Deposit>>
    get() = depositRepository.deposits.map { deposits ->
      deposits.asFlow()
        .map { deposit ->
          val term = depositRepository.getTerm(deposit.id).first()
          Deposit(
            deposit.id,
            deposit.balance,
            enumValueOf(deposit.currency),
            deposit.status,
            deposit.name,
            term.rate,
            term.closeDate
          )
        }
        .toList()
    }
}