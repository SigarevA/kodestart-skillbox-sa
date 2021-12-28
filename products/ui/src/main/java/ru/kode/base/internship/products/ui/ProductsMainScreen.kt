package ru.kode.base.internship.products.ui

import androidx.compose.runtime.Immutable
import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.Deposit

internal object ProductsMainScreen {
  class ViewIntents : BaseViewIntents() {
    val clickExpand = intent<Int>(name = "clickExpand")
    val navigateCard = intent<Card>(name = "navigateCard")
    val navigateDeposit = intent<Deposit>(name = "navigateDeposit")
    val openNewAccountOrDeposit = intent<Unit>(name = "openNewAccountOrDeposit")
    val refresh = intent(name = "refresh")
  }

  @Immutable
  data class ViewState(
    val bankAccountsLceState: LceState = LceState.None,
    val depositsLceState: LceState = LceState.None,
    val deposits: List<Deposit>? = null,
    val bankAccounts: BankAccountInfo? = null,
    val errorMessage: ErrorMessage? = null,
    val isRefreshAccount: Boolean = false,
    val isRefreshDeposit: Boolean = false,
  ) {
    val isRefresh: Boolean
      get() = isRefreshAccount || isRefreshDeposit
  }

  internal data class BankAccountInfo(
    val bankAccounts: List<Account>,
    val expandBankAccounts: List<Boolean>,
  )

  sealed class ErrorMessage { }
}