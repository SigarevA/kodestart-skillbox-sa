package ru.kode.base.intership.data.products.deposits

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import ru.kode.base.intership.data.di.ProductsDataModule.Companion.SIMPLE_DATE_TERM
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.products.data.ProductsDataBase
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

internal class DepositRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val db: ProductsDataBase,
  @Named(SIMPLE_DATE_TERM) private val formatDate: SimpleDateFormat,
) : DepositRepository {
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
    if (!isRefresh) {
      val deposits = productsAPI.fetchDeposit().deposits
      depositEntityQueries.transaction {
        deposits.forEach {
          depositEntityQueries.insertDeposit(
            it.depositId,
            it.balance,
            it.currency,
            it.status,
            it.name
          )
        }
      }
    }
  }

  override suspend fun getTerm(depositId: Long, isRefresh: Boolean): Flow<DepositTerm> {
    if (!isRefresh) {
      val term = productsAPI.fetchDepositTerms(depositId, "android-$depositId")
      val date: Long = formatDate.parse(term.closeDate)?.time ?: throw IllegalStateException()
      depositTermsQueries.insertDepositTerm(depositId,
        date,
        term.rate)
    }
    return depositTermsQueries.getDepositTermByDepositID(depositId,
      mapper = { _, close, rate -> DepositTerm(Date(close), rate.toFloat() + (Math.random() * 7.0f).toFloat()) })
      .asFlow()
      .mapToOne()
  }
}