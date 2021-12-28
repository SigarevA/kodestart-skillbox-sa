package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.mappers.toDomainDeposit
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import timber.log.Timber
import javax.inject.Inject

internal class ProductsUseCaseImpl @Inject constructor(
  private val accountRepository: AccountRepository,
  private val depositRepository: DepositRepository,
  private val cardRepository: CardRepository,
  scope: CoroutineScope,
) : BaseUseCase<ProductsUseCaseImpl.State>(scope, State()), ProductsUseCase {

  data class State(
    val bankAccountState: LceState = LceState.None,
    val depositState: LceState = LceState.None,
  )

  override suspend fun loadBankAccount(isRefresh: Boolean) {
    Timber.d("invoke load accounts")
    setState { copy(bankAccountState = LceState.Loading) }
    Timber.d("invoke load accounts - 2")
    try {
      accountRepository.load(isRefresh)
      setState { copy(bankAccountState = LceState.Content) }
    } catch (e: Exception) {
      setState { copy(bankAccountState = LceState.Error(e.message)) }
    }
  }

  override suspend fun loadDeposits(isRefresh: Boolean) {
    Timber.d("invoke load deposits")
    setState { copy(depositState = LceState.Loading) }
    Timber.d("invoke load deposits - 2")
    try {
      depositRepository.load(isRefresh)
      setState { copy(depositState = LceState.Content) }
    } catch (e: Exception) {
      setState { copy(depositState = LceState.Error(e.message)) }
    }
  }

  override val accountState: Flow<LceState>
    get() = stateFlow.map { it.bankAccountState }.distinctUntilChanged()

  override val depositState: Flow<LceState>
    get() = stateFlow.map { it.depositState }
      .onEach {
        Timber.d("depositState : $it")
      }.distinctUntilChanged()

  override val depositsFlow: Flow<List<Deposit>>
    get() = depositRepository.deposits.flatMapMerge { generalDeposits ->
      val terms = generalDeposits.map {
        depositRepository.getTerm(it.id)
      }
      combine(terms) { depositTerms ->
        generalDeposits.mapIndexed { i, deposit ->
          deposit.toDomainDeposit(depositTerms[i])
        }
      }
    }

  override val accountsFlow: Flow<List<Account>>
    get() = accountRepository.accounts
      .flatMapMerge { accounts ->
        val detailCardFlows = accounts
          .fold(mutableListOf<Long>()) { acc, generalAccount ->
            acc.addAll(generalAccount.cards)
            acc
          }
          .map { cardId ->
            cardRepository.cardDetails(cardId)
          }
        combine(detailCardFlows) { allCard ->
          accounts.map {
            val accountCards = allCard.filter { card -> it.cards.contains(card.id) }
            Account(
              it.id,
              it.number,
              it.balance,
              enumValueOf(it.currency),
              it.status,
              accountCards
            )
          }
        }
      }
}