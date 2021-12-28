package ru.kode.base.intership.data.products.mappers

import rukodebaseintershipproductsdata.CardEntity
import java.util.Date

typealias DomainCard = ru.kode.base.internship.domain.Card

fun CardEntity.toDomainCard(): DomainCard =
  DomainCard(
    this.id,
    this.name,
    enumValueOf(this.type),
    this.number,
    enumValueOf(this.status),
    enumValueOf(this.paymentSystem),
    Date(this.expiredAt)
  )