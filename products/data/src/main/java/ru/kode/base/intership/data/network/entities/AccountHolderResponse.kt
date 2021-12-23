package ru.kode.base.intership.data.network.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AccountHolderResponse(
  val accounts: List<AccountResponse>,
)