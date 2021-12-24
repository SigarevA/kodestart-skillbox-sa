package ru.kode.base.internship.products.domain.mappers

import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit

fun GeneralDeposit.toDomainDeposit(term : DepositTerm) : Deposit =
  Deposit(
    id,
    balance,
    enumValueOf(currency),
    status,
    name,
    term.rate,
    term.closeDate
  )