package ru.kode.base.internship.products.domain.models

import ru.kode.base.internship.domain.Card

data class Account(
  val id: Long,
  val number: String,
  val balance: Double,
  val currency: CurrencyType,
  val status: String,
  val cards: List<Card>,
)