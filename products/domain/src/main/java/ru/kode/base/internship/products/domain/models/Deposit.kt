package ru.kode.base.internship.products.domain.models

import java.util.Date

data class Deposit(
  val id: Long,
  val balance: Double,
  val currency: CurrencyType,
  val status: String,
  val name: String?,
  val rate: Float,
  val closeDate: Date,
)