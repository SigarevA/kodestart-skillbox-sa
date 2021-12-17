package ru.kode.base.internship.utils

import ru.kode.base.internship.products.model.CurrencyType
import ru.kode.base.internship.products.model.Deposit
import ru.kode.base.internship.products.ui.R

internal val Deposit.icon: Int
  get() = currencyType.icon

internal val CurrencyType.icon: Int
  get() =
    when (this) {
      CurrencyType.Euro -> R.drawable.ic_euro
      CurrencyType.Dollar -> R.drawable.ic_dollar
      CurrencyType.Ruble -> R.drawable.ic_ruble
    }
