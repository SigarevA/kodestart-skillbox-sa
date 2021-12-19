package ru.kode.base.internship.domain

import ru.kode.base.internship.core.domain.entity.PaymentSystem
import java.util.Date

data class Card(
  val id: Long,
  val name: String,
  val type: Type,
  val number: String,
  val status: Status,
  val paymentSystem: PaymentSystem,
  val expiredAt: Date,
) {
  enum class Status {
    ACTIVE, DEACTIVATED
  }

  enum class Type {
    physical, digital
  }
}