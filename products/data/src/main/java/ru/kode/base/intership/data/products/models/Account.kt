package ru.kode.base.intership.data.products.models

data class Account(
  val id: Long,
  val number: String,
  val balance: Double,
  val currency: String,
  val status: String,
  val cards: List<Long>,
)