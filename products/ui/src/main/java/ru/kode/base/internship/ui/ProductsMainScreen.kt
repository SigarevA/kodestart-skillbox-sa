package ru.kode.base.internship.ui

import androidx.compose.runtime.Immutable
import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.model.BankAccount
import ru.kode.base.internship.products.model.Card
import ru.kode.base.internship.products.model.Deposit

internal object ProductsMainScreen {
  class ViewIntents : BaseViewIntents() {
    val clickExpand = intent<Int>(name = "clickExpand")
    val navigateCard = intent<Card>(name = "navigateCard")
    val navigateDeposit = intent<Deposit>(name = "navigateDeposit")
    val openNewAccountOrDeposit = intent<Unit>(name = "openNewAccountOrDeposit")
  }

  @Immutable
  data class ViewState(
    val bankAccountsLceState: LceState = LceState.None,
    val depositsLceState: LceState = LceState.None,
    val deposits: List<Deposit>? = null,
    val bankAccounts: BankAccountInfo? = null,
    val errorMessage: ErrorMessage? = null,
  )

  internal data class BankAccountInfo(
    val bankAccounts: List<BankAccount>,
    val expandBankAccounts: List<Boolean>,
  )

  sealed class ErrorMessage {

  }
}