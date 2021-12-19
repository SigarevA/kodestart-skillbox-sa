package ru.kode.base.internship.products.domain.models

data class GeneralDeposit(
  val id: Long,
  val balance: Double,
  val currency: String,
  val status: String,
  val name: String?,
)