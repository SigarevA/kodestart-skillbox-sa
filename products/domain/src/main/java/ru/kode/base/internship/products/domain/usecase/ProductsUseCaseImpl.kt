package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.products.domain.mappers.toAccount
import ru.kode.base.internship.products.domain.mappers.toDomainDeposit
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.domain.models.GeneralAccount
import ru.kode.base.internship.products.domain.models.GeneralDeposit
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import timber.log.Timber
import javax.inject.Inject

internal class ProductsUseCaseImpl @Inject constructor(
  private val accountRepository: AccountRepository,
  private val depositRepository: DepositRepository,
  private val cardRepository: CardRepository,
  private val scope: CoroutineScope,
) : BaseUseCase<ProductsUseCaseImpl.State>(scope, State()), ProductsUseCase {

  data class State(
    val bankAccountState: LceState = LceState.None,
    val depositState: LceState = LceState.None,
    val numberOfLoadedCards: Int = 0,
    val numberOfAllCards: Int = 0,
    val accounts: List<Account> = emptyList(),
    val deposits: List<Deposit> = emptyList(),
    val accountJob: Job? = null,
    val depositJob: Job? = null,
    val terms: List<Job> = emptyList(),
    val cardsState: List<Job> = emptyList(),
  )

  override suspend fun loadBankAccount(isRefresh: Boolean) {
    Timber.d("as")
    setState { copy(bankAccountState = LceState.Loading) }
    withState {
      it.accountJob?.cancel()
    }
    val job = accountRepository.accounts
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
    setState {
      copy(accountJob = job)
    }
    accountRepository.load(isRefresh)
  }

  private suspend fun loadCards(accounts: List<GeneralAccount>, isRefresh: Boolean) {
    withState { state ->
      state.cardsState.forEach { it.cancel() }
    }
    val accCardJob = ArrayList<Job>()
    accounts.forEach { account ->
      account.cards.forEach { cardId ->
        accCardJob.add(processCard(account.id, cardId, isRefresh))
      }
    }
    setState {
      copy(cardsState = accCardJob)
    }
  }

  private suspend fun processCard(accountId: Long, cardId: Long, isRefresh: Boolean): Job {
    return cardRepository.cardDetails(cardId, isRefresh)
      .onEach { card ->
        var flag = true
        setState {
          copy(accounts = this.accounts
            .map {
              if (it.id == accountId) {
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
            },
            numberOfLoadedCards = numberOfLoadedCards + if (flag) 1 else 0,
            bankAccountState = LceState.Content
          )
        }
      }
      .launchIn(scope)
  }

  override suspend fun loadDeposits(isRefresh: Boolean) {
    setState { copy(depositState = LceState.Loading) }
    try {
      depositRepository.deposits
        .onEach {
          setState {
            copy(depositState = LceState.Content)
          }
          processLoadDepositTerms(it, isRefresh)
        }
        .launchIn(scope)
      depositRepository.load(isRefresh)
    } catch (e: Exception) {
      setState { copy(depositState = LceState.Error(e.message)) }
    }
  }

  private suspend fun processLoadDepositTerms(deposits: List<GeneralDeposit>, isRefresh: Boolean) {
    withState { state ->
      state.terms.forEach { job -> job.cancel() }
    }
    val termsHolder = ArrayList<Job>(deposits.size)
    deposits.forEach { deposit ->
      termsHolder.add(
        depositRepository.getTerm(deposit.id, isRefresh)
          .onEach { depositTerm ->
            val newDeposits = withState { state ->
              var isAdd = true
              val result = state.deposits.map {
                if (it.id == deposit.id) {
                  isAdd = false
                  it.copy(rate = depositTerm.rate, closeDate = depositTerm.closeDate)
                } else
                  it
              }.let {
                if (isAdd)
                  it.plus(deposit.toDomainDeposit(depositTerm))
                else
                  it
              }
              result
            }
            setState {
              copy(deposits = newDeposits)
            }
          }
          .catch { e ->
            setState {
              copy(depositState = LceState.Error(e.message))
            }
          }
          .launchIn(scope)
      )
    }
    setState {
      copy(terms = termsHolder)
    }
  }

  override val accountState: Flow<LceState>
    get() = stateFlow.map { it.bankAccountState }.distinctUntilChanged()
  override val depositState: Flow<LceState>
    get() = stateFlow.map { it.depositState }.distinctUntilChanged()

  override val accountsFlow: Flow<List<Account>>
    get() = stateFlow.map { it.accounts }.distinctUntilChanged()

  override val depositsFlow: Flow<List<Deposit>>
    get() = stateFlow.map { it.deposits }.distinctUntilChanged()
}