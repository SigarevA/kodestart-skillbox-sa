package ru.sigarev.products.utils

import ru.sigarev.products.model.CardStatus
import ru.sigarev.products.model.CardType
import ru.sigarev.products.model.PaymentSystem
import ru.sigarev.products.ui.R

internal val CardType.string
  get() = when (this) {
    CardType.Additional -> R.string.products_additional_card
    CardType.Salary -> R.string.products_salary_card
  }

internal val PaymentSystem.drawable
  get() = when (this) {
    PaymentSystem.MasterCard -> R.drawable.ic_mastercard
    PaymentSystem.Visa -> R.drawable.ic_visa
  }

internal val CardStatus.string
  get() = when (this) {
    CardStatus.Virtual -> R.string.products_virtual_card
    CardStatus.Physical -> R.string.products_physical_card
    CardStatus.Locked -> R.string.products_blocked_card
  }