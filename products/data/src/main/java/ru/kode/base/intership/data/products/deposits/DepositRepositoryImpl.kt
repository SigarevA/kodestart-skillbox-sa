package ru.kode.base.intership.data.products.deposits

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.data.products.FakeData
import ru.kode.base.intership.products.data.ProductsDataBase

import javax.inject.Inject

internal class DepositRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val db : ProductsDataBase
) : DepositRepository {
  private val _deposits: MutableSharedFlow<List<GeneralDeposit>> = MutableSharedFlow()

  override val deposits: Flow<List<GeneralDeposit>>
    get() = _deposits.asSharedFlow()

  override suspend fun load() {
    // delay(5_400)
    val deposits = productsAPI.fetchDeposit().deposits
    _deposits.emit(deposits.map {
      GeneralDeposit(
        it.depositId,
        it.balance,
        "RUB",
        "Активен",
        "Накопительный"
      )
    })
  }

  override fun getTerm(depositId: Long): Flow<DepositTerm> = flow {
    val term = FakeData.termsForDeposits[depositId] ?: throw IllegalArgumentException()
    val r = (Math.random() * 6) + 6
    emit(term.copy(rate = r.toFloat()))
  }
}