package ru.kode.base.internship.products.model

data class Card(
  val name: String,
  val type: Type,
  val shortNumber: String,
  val status: Status,
  val paymentSystem: PaymentSystem,
) {

  enum class Status {
    Active, Deactivated
  }

  enum class Type {
    Physical, Digital
  }
}

enum class PaymentSystem {
  Visa, MasterCard
}