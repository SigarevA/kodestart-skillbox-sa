package ru.kode.base.internship.products.domain.mappers

import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.GeneralAccount

fun GeneralAccount.toAccount(cards: List<Card> = emptyList()): Account =
  Account(
    id,
    number,
    balance,
    enumValueOf(currency),
    status,
    emptyList()
  )
