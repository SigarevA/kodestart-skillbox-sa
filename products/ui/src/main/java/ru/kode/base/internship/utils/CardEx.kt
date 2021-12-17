package ru.kode.base.internship.utils

import ru.kode.base.internship.products.model.Card.Type
import ru.kode.base.internship.products.model.PaymentSystem
import ru.kode.base.internship.products.ui.R

val Type.stringId
  get() = when (this) {
    Type.Digital -> R.string.products_virtual_card
    Type.Physical -> R.string.products_physical_card
  }

internal val PaymentSystem.drawable
  get() = when (this) {
    PaymentSystem.MasterCard -> R.drawable.ic_mastercard
    PaymentSystem.Visa -> R.drawable.ic_visa
  }