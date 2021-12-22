package ru.kode.base.internship.products.domain.fakeusecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.products.domain.mappers.toAccount
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.domain.models.GeneralAccount
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import timber.log.Timber
import javax.inject.Inject

internal class FakeProductsUseCase @Inject constructor(
  private val accountRepository: AccountRepository,
  private val depositRepository: DepositRepository,
  private val cardRepository: CardRepository,
  private val scope: CoroutineScope,
) : BaseUseCase<FakeProductsUseCase.State>(scope, State()), ProductsUseCase {

  data class State(
    val bankAccountState: LceState = LceState.None,
    val depositState: LceState = LceState.None,
    val accounts: List<Account> = emptyList(),
  )

  override suspend fun loadBankAccount(isRefresh: Boolean) {
    setState { copy(bankAccountState = LceState.Loading) }
    accountRepository.accounts
      .onEach { accounts ->
        setState {
          copy(
            bankAccountState = LceState.Content,
            accounts = accounts.map { it.toAccount() }
          )
        }
        loadCards(accounts, isRefresh)
      }
      .catch { e ->
        setState { copy(bankAccountState = LceState.Error(e.message)) }
        Timber.e(e, e.message)
      }
      .launchIn(scope)
    accountRepository.load(isRefresh)
  }

  private suspend fun loadCards(accounts: List<GeneralAccount>, isRefresh: Boolean) {
    accounts.forEach { account ->
      account.cards.forEach { cardId ->
        cardRepository.cardDetails(cardId, isRefresh)
          .onEach { card ->
            setState {
              copy(accounts = this.accounts.map {
                if (it.id == account.id) {
                  var flag = true
                  val newCards = ArrayList<Card>().apply {
                    addAll(
                      it.cards.map { oldCard ->
                        if (oldCard.id == card.id) {
                          flag = false
                          card
                        } else
                          oldCard
                      }
                    )
                    if (flag)
                      add(card)
                  }
                  it.copy(cards = newCards)
                } else
                  it
              }
              )
            }
          }
          .launchIn(scope)
      }
    }
  }

  override suspend fun loadDeposits(isRefresh: Boolean) {
    if (!isRefresh)
      setState { copy(depositState = LceState.Loading) }
    try {
      depositRepository.deposits
        .onEach { deposits ->
          deposits.forEach { deposit ->
            depositRepository.getTerm(deposit.id)
              .onEach {

              }
              .catch { }
              .launchIn(scope)
          }
        }
        .launchIn(scope)
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
    get() = stateFlow.map { it.accounts }.distinctUntilChanged()

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