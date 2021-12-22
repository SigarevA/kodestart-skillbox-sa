package ru.kode.base.intership.data.network.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DepositTermResponse(
  val name : String,
  val balance : Double,
  val currency : String,
  val rate : Double,
  val status : String,
  val closeDate : String
)