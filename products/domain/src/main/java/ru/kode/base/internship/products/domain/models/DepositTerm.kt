package ru.kode.base.internship.products.domain.models

import java.util.Date

data class DepositTerm(
  val closeDate: Date,
  val rate: Float,
)