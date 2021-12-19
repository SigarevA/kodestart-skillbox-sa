package ru.kode.base.internship.products.utils

import ru.kode.base.internship.core.domain.entity.PaymentSystem
import ru.kode.base.internship.domain.Card.Type
import ru.kode.base.internship.products.ui.R

val Type.stringId
  get() = when (this) {
    Type.digital -> R.string.products_virtual_card
    Type.physical -> R.string.products_physical_card
  }

internal val PaymentSystem.drawable
  get() = when (this) {
    PaymentSystem.MasterCard -> R.drawable.ic_mastercard
    PaymentSystem.Visa -> R.drawable.ic_visa
  }