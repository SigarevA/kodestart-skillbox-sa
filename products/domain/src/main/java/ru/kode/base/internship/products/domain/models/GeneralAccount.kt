package ru.kode.base.internship.products.domain.models

data class GeneralAccount(
  val id: Long,
  val number: String,
  val balance: Double,
  val currency: String,
  val status: String,
  val cards: List<Long>,
)