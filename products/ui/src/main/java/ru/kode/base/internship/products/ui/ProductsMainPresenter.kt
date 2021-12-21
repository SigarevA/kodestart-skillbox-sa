package ru.kode.base.internship.products.ui

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.dimsuz.unicorn.coroutines.MachineDsl
import ru.kode.base.core.coroutine.BasePresenter
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.fakeusecase.ProductsUseCase
import ru.kode.base.internship.routing.AppFlow
import javax.inject.Inject

internal class ProductsMainPresenter @Inject constructor(
  private val productsUseCase: ProductsUseCase,
  private val coordinator: AppFlow.Coordinator,
) :
  BasePresenter<ProductsMainScreen.ViewState, ProductsMainScreen.ViewIntents, Unit>() {

  override fun MachineDsl<ProductsMainScreen.ViewState, Unit>.buildMachine() {
    initial = ProductsMainScreen.ViewState() to {
      launch {
        val banks = async {
          productsUseCase.loadBankAccount(false)
        }
        val deposits = async {
          productsUseCase.loadDeposits(false)
        }
        awaitAll(banks, deposits)
      }
    }

    onEach(productsUseCase.depositsFlow) {
      transitionTo { state, deposits ->
        if (deposits.isNotEmpty()) {
          state.copy(
            deposits = deposits,
            isRefreshDeposit = !state.depositsLceState.isComplete && state.isRefreshDeposit
          )
        } else
          state
      }
    }

    onEach(intent(ProductsMainScreen.ViewIntents::clickExpand)) {
      transitionTo { state, position ->
        state.bankAccounts?.let {
          state.copy(
            bankAccounts = it.copy(expandBankAccounts = it.expandBankAccounts.mapIndexed { i, v ->
              if (i == position) !v else v
            })
          )
        } ?: state
      }
    }

    onEach(intent(ProductsMainScreen.ViewIntents::refresh)) {
      transitionTo { state, _ ->
        if (state.isComplete && !state.isRefresh)
          state.copy(isRefreshAccount = true, isRefreshDeposit = true)
        else
          state
      }

      action { _, newState, _ ->
        if (newState.isRefresh) {
          executeAsync {
            val banks = async {
              productsUseCase.loadBankAccount(newState.bankAccountsLceState == LceState.Content)
            }
            val deposits = async {
              productsUseCase.loadDeposits(newState.depositsLceState == LceState.Content)
            }
            awaitAll(banks, deposits)
          }
        }
      }
    }

    onEach(productsUseCase.accountState) {
      transitionTo { state, payload ->
        if (payload.isComplete && state.isRefreshAccount)
          state.copy(depositsLceState = payload, isRefreshAccount = false)
        else
          state.copy(bankAccountsLceState = payload)
      }
    }

    onEach(productsUseCase.depositState) {
      transitionTo { state, payload ->
        if (payload.isComplete && state.isRefreshDeposit)
          state.copy(depositsLceState = payload, isRefreshDeposit = false)
        else
          state.copy(depositsLceState = payload)
      }
    }

    onEach(productsUseCase.accountsFlow) {
      transitionTo { state, accounts ->
        if (accounts.isNotEmpty()) {
          state.copy(
            bankAccounts = ProductsMainScreen.BankAccountInfo(
              accounts, ArrayList<Boolean>().apply {
                repeat(accounts.size) {
                  add(true)
                }
              }
            ),
            isRefreshAccount = !state.bankAccountsLceState.isComplete && state.isRefreshAccount
          )
        } else
          state
      }
    }

    onEach(intent(ProductsMainScreen.ViewIntents::navigateDeposit)) {
      action { state, newState, payload ->

      }
    }

    onEach(intent(ProductsMainScreen.ViewIntents::openNewAccountOrDeposit)) {
      action { state, newState, payload ->

      }
    }

    onEach(intent(ProductsMainScreen.ViewIntents::navigateCard)) {
      transitionTo { state, _ ->
        state
      }

      action { _, _, payload ->
        coordinator.handleEvent(AppFlow.Event.CardDetailRequest(payload.id))
      }
    }
  }

  private val ProductsMainScreen.ViewState.isComplete: Boolean
    get() = depositsLceState.isComplete && bankAccountsLceState.isComplete

  private val LceState.isComplete: Boolean
    get() = this != LceState.None && this != LceState.Loading
}