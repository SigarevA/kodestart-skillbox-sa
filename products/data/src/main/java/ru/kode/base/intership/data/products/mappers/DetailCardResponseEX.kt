package ru.kode.base.intership.data.products.mappers

import ru.kode.base.intership.data.network.entities.DetailCardResponse
import ru.kode.base.intership.data.products.models.Card
import java.util.Date

fun DetailCardResponse.toCard() =
  Card(
    this.id,
    0,
    this.name,
    "physical",
    "7789778977897789",
    "ACTIVE",
    "Visa",
    Date(1725107846000)
  )
