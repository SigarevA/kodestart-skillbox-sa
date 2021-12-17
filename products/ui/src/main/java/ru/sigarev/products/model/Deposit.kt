package ru.sigarev.products.model

internal data class Deposit(
  val id: Long,
  val name: String,
  val sum: String,
  val bid: String,
  val validityUpTo: String,
  val currencyType: CurrencyType,
)

enum class CurrencyType {
  Ruble, Dollar, Euro
}