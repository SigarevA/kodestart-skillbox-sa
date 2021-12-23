package ru.kode.base.intership.data.products.mappers

import ru.kode.base.intership.data.network.entities.DetailCardResponse
import ru.kode.base.intership.data.products.models.Card
import java.util.Date

fun DetailCardResponse.toCard() =
  Card(
    this.id,
    accountId,
    this.name,
    "physical",
    this.number,
    status,
    "Visa",
    Date(1725107846000)
  )
