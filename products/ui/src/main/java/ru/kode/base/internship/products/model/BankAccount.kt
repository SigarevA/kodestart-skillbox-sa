package ru.kode.base.internship.products.model

internal class BankAccount(
  val sum: String,
  val currency: CurrencyType,
  val cards: List<Card>,
)