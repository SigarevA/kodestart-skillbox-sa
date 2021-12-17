package ru.sigarev.products.model

internal data class Card(
  val cardType: CardType,
  val shortNumber: String,
  val status: CardStatus,
  val paymentSystem: PaymentSystem,
)

enum class CardStatus {
  Physical, Locked, Virtual
}

enum class PaymentSystem {
  Visa, MasterCard
}

enum class CardType {
  Salary, Additional
}