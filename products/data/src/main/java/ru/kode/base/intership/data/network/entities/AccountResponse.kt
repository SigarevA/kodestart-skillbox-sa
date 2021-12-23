package ru.kode.base.intership.data.network.entities

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class AccountResponse(
  val accountId: Long,
  val number: String,
  val balance: Double,
  val currency: String,
  val status: String,
  val cards: List<CardInAccountResponse>,
)