package ru.kode.base.intership.data.products

import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralAccount
import ru.kode.base.internship.products.domain.models.GeneralDeposit
import ru.kode.base.intership.data.products.models.Card

import java.util.Date

internal object FakeData {
  val accounts = mutableListOf(
    GeneralAccount(
      2, "0467683482412661", 1_515_000.2, "RUB", "Активен", listOf(0, 1)
    )
  )

  val deposits = mutableListOf(
    GeneralDeposit(
      0, 1_515_000.78, "RUB", "Активен", "Мой вклад"
    ),
    GeneralDeposit(
      1, 3_719.19, "DOLLAR", "Активен", "Накопительный"
    ),
    GeneralDeposit(
      2, 1_513.62, "EURO", "Активен", "Накопительный"
    )
  )

  val cards = mutableListOf(
    Card(0, 0, "Карта зарплатная", "physical", "7789778977897789", "ACTIVE", "MasterCard", Date(1725107846000)),
    Card(1, 0, "Дополнительная карта", "physical", "8435843584358435", "DEACTIVATED", "Visa", Date(1725107846000))
  )

  val termsForDeposits = mapOf(
    0L to DepositTerm(Date(1725107846000), 8.65f),
    1L to DepositTerm(Date(1756643846000), 11.5f),
    2L to DepositTerm(Date(1788179846000), 7.65f)
  )
}