package ru.kode.base.intership.data.products.mappers

import ru.kode.base.intership.data.products.models.Card
import rukodebaseintershipproductsdata.CardEntity
import java.util.Date


typealias DomainCard = ru.kode.base.internship.domain.Card

fun Card.toDomainCard(): DomainCard =
  DomainCard(
    this.id,
    this.name,
    enumValueOf(this.type),
    this.number,
    enumValueOf(this.status),
    enumValueOf(this.paymentSystem),
    this.expiredAt
  )

fun CardEntity.toDomainCard(): DomainCard =
  DomainCard(
    this.id,
    this.name,
    enumValueOf(this.type),
    this.number,
    enumValueOf(this.status),
    enumValueOf(this.paymentSystem),
    Date()
  )