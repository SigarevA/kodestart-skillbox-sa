package ru.kode.base.intership.data.network.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DetailCardResponse(
  val id: Long,
  val accountId: Long,
  val number : String,
  val expiredAt: String,
  val paymentSystem: String,
  val status: String,
  val name: String,
)