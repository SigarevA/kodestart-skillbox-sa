package ru.kode.base.intership.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.kode.base.intership.data.network.entities.AccountHolderResponse
import ru.kode.base.intership.data.network.entities.DepositHolderResponse
import ru.kode.base.intership.data.network.entities.DetailCardResponse

internal interface ProductsAPI {
  @GET("api/core/account/list")
  suspend fun fetchAccounts(): AccountHolderResponse

  @GET("api/core/card/{card_id}")
  suspend fun fetchDetailCard(
    @Path("card_id") cardId: Long,
  ): DetailCardResponse

  @GET("api/core/deposit/list")
  suspend fun fetchDeposit(): DepositHolderResponse
}