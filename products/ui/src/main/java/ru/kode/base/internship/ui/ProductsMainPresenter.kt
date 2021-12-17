package ru.kode.base.internship.ui

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import ru.dimsuz.unicorn.coroutines.MachineDsl
import ru.kode.base.core.coroutine.BasePresenter
import ru.kode.base.internship.products.domain.fakeusecase.ProductsUseCase
import ru.kode.base.internship.products.model.Card
import ru.kode.base.internship.products.model.CurrencyType
import ru.kode.base.internship.products.model.Deposit
import ru.kode.base.internship.products.model.PaymentSystem
import ru.kode.base.internship.routing.AppFlow
import javax.inject.Inject

private typealias Account = ru.kode.base.internship.products.model.BankAccount
private typealias BankCard = ru.kode.base.internship.products.model.Card

internal class ProductsMainPresenter @Inject constructor(
  private val productsUseCase: ProductsUseCase,
  private val coordinator: AppFlow.Coordinator,
) :
  BasePresenter<ProductsMainScreen.ViewState, ProductsMainScreen.ViewIntents, Unit>() {

  override fun MachineDsl<ProductsMainScreen.ViewState, Unit>.buildMachine() {
    initial = ProductsMainScreen.ViewState() to {
      launch {
        val banks = async {
          productsUseCase.loadBankAccount()
        }
        val deposits = async {
          productsUseCase.loadDeposits()
        }
        awaitAll(banks, deposits)
      }
    }

    onEach(productsUseCase.depositsFlow) {
      transitionTo { state, deposits ->
        if (deposits.isNotEmpty())
          state.copy(
            deposits = listOf(
              Deposit(1, "Мой вклад", "457 334,00 ₽", "Ставка 7.65%", "до 31.08.2024", CurrencyType.Ruble),
              Deposit(2, "Накопительный", "3 719,19 \$", "Ставка 11.05%", "до 31.08.2024", CurrencyType.Dollar),
              Deposit(3, "EUR вклад", "1 513,62 €", "Ставка 8.65%", "до 31.08.2026", CurrencyType.Euro)
            )
          )
        else
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

    onEach(productsUseCase.accountState) {
      transitionTo { state, payload ->
        state.copy(bankAccountsLceState = payload)
      }
    }

    onEach(productsUseCase.depositState) {
      transitionTo { state, payload ->
        state.copy(depositsLceState = payload)
      }
    }

    onEach(productsUseCase.accountsFlow) {
      transitionTo { state, accounts ->
        if (accounts.isNotEmpty())
          state.copy(
            bankAccounts = ProductsMainScreen.BankAccountInfo(
              accounts.map {
                Account("457 334,00 ₽", CurrencyType.Ruble, listOf(
                  BankCard("Карта зарплатная",
                    Card.Type.Physical,
                    "7789",
                    Card.Status.Active,
                    PaymentSystem.Visa),
                  BankCard("Дополнительная карта",
                    Card.Type.Physical,
                    "8435",
                    Card.Status.Deactivated,
                    PaymentSystem.MasterCard
                  )
                )
                )
              }, ArrayList<Boolean>().apply {
                repeat(accounts.size) {
                  add(true)
                }
              }
            )
          )
        else
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
      action { state, newState, payload ->

      }
    }
  }

  companion object {
    private const val TAG = "ProductController"
  }
}