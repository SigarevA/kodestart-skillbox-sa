package ru.kode.base.intership.data.products.models

import java.util.Date

data class Card(
  val id: Long,
  val accountId: Long = 0,
  val name: String,
  val type: String,
  val number: String,
  val status: String,
  val paymentSystem: String,
  val expiredAt: Date,
)