package ru.kode.base.intership.data.products.deposits

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.products.data.ProductsDataBase
import java.util.Date
import javax.inject.Inject

internal class DepositRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val db: ProductsDataBase,
) : DepositRepository {
  private val _deposits: MutableSharedFlow<List<GeneralDeposit>> = MutableSharedFlow()
  private val depositEntityQueries = db.depositEntityQueries
  private val depositTermsQueries = db.depositTermQueries

  private val allDeposits = depositEntityQueries.getAllDeposits().asFlow().mapToList().map { depositEntities ->
    depositEntities.map {
      GeneralDeposit(
        it.id,
        it.balance,
        it.currency,
        it.status,
        it.name
      )
    }
  }

  override val deposits: Flow<List<GeneralDeposit>>
    get() = allDeposits

  override suspend fun load(isRefresh: Boolean) {
    if (isRefresh) {
      val deposits = productsAPI.fetchDeposit().deposits
      depositEntityQueries.transaction {
        deposits.forEach {
          depositEntityQueries.insertDeposit(
            it.depositId,
            it.balance,
            "RUB",
            "Активен",
            "Накопительный"
          )
        }
      }
    }
  }

  override suspend fun getTerm(depositId: Long, isRefresh: Boolean): Flow<DepositTerm> {
    if (isRefresh) {
      val term = productsAPI.fetchDepositTerms(depositId)
      term?.let {
        depositTermsQueries.insertDepositTerm(depositId, 3213, it.rate)
      } ?: run {
        depositTermsQueries.insertDepositTerm(depositId, 321233213, 11.1)
      }
    }
    return depositTermsQueries.getDepositTermByDepositID(depositId,
      mapper = { _, _, rate -> DepositTerm(Date(321), rate.toFloat()) })
      .asFlow()
      .mapToOne()
  }

  /*flow {
    val term = FakeData.termsForDeposits[depositId] ?: throw IllegalArgumentException()
    val r = (Math.random() * 6) + 6
    emit(term.copy(rate = r.toFloat()))
  }

   */
}