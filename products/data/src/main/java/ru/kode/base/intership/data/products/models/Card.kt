package ru.kode.base.intership.data.products.models

import java.util.Date

data class Card(
  val id: Long,
  val name: String,
  val type: String,
  val number: String,
  val status: String,
  val paymentSystem: String,
  val expiredAt : Date
)