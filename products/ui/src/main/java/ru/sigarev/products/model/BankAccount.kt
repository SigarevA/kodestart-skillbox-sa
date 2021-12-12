package ru.sigarev.products.model

internal class BankAccount(
  val sym: String,
  val currency: CurrencyType,
  val cards: List<Card>,
)