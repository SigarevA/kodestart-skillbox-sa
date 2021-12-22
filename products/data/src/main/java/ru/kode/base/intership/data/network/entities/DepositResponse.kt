package ru.kode.base.intership.data.network.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DepositResponse(
  val depositId: Long,
  val balance: Double,
  val currency: String,
  val status: String,
  val name: String,
)