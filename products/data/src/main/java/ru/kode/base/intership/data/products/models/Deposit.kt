package ru.kode.base.intership.data.products.models

data class Deposit(
  val id: Long,
  val balance: Double,
  val currency: String,
  val status: String,
  val name: String?,
)