package ru.kode.base.internship.products.utils

import ru.kode.base.internship.products.domain.models.CurrencyType
import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.ui.R


internal val Deposit.icon: Int
  get() = currency.icon

internal val CurrencyType.icon: Int
  get() =
    when (this) {
      CurrencyType.EURO -> R.drawable.ic_euro
      CurrencyType.DOLLAR -> R.drawable.ic_dollar
      CurrencyType.RUB -> R.drawable.ic_ruble
    }
