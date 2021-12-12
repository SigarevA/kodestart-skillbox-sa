package ru.sigarev.products.utils

import ru.sigarev.products.model.CurrencyType
import ru.sigarev.products.model.Deposit
import ru.sigarev.products.ui.R

internal val Deposit.icon: Int
  get() = currencyType.icon

internal val CurrencyType.icon: Int
  get() =
    when (this) {
      CurrencyType.Euro -> R.drawable.ic_euro
      CurrencyType.Dollar -> R.drawable.ic_dollar
      CurrencyType.Ruble -> R.drawable.ic_ruble
    }
