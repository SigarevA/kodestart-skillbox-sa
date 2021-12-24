package ru.kode.base.intership.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kode.base.intership.data.network.entities.AccountHolderResponse
import ru.kode.base.intership.data.network.entities.DepositHolderResponse
import ru.kode.base.intership.data.network.entities.DepositTermResponse
import ru.kode.base.intership.data.network.entities.DetailCardResponse

internal interface ProductsAPI {
  @GET("api/core/account/list")
  suspend fun fetchAccounts(
    @Query("__example") example: String = "android",
  ): AccountHolderResponse

  @GET("api/core/card/{card_id}")
  suspend fun fetchDetailCard(
    @Path("card_id") cardId: Long,
    @Query("__example") card: String,
  ): DetailCardResponse

  @GET("api/core/deposit/list")
  suspend fun fetchDeposit(
    @Query("__example") example: String = "android",
  ): DepositHolderResponse

  @GET("api/core/deposit/{deposit_id}")
  suspend fun fetchDepositTerms(
    @Path("deposit_id") depositId: Long,
    @Query("__example") example: String,
  ): DepositTermResponse
}