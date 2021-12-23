package ru.kode.base.intership.data.network.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kode.base.internship.core.domain.entity.PaymentSystem
import ru.kode.base.internship.domain.Card

@JsonClass(generateAdapter = true)
class CardInAccountResponse(
  @Json(name = "card_id")
  val cardId: String,
  val number: String,
  val status: Card.Status,
  val name: String,
  @Json(name = "payment_system")
  val paymentSystem: PaymentSystem,
  @Json(name = "card_type")
  val cardType: Card.Type,
)