package ru.kode.base.intership.data.products.deposits

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit
import ru.kode.base.internship.products.domain.repositories.DepositRepository
import ru.kode.base.intership.data.products.FakeData

import javax.inject.Inject

internal class DepositRepositoryImpl @Inject constructor() : DepositRepository {
  private val _deposits: MutableSharedFlow<List<GeneralDeposit>> = MutableSharedFlow()

  override val deposits: Flow<List<GeneralDeposit>>
    get() = _deposits.asSharedFlow()

  override suspend fun load() {
    delay(5_400)
    _deposits.emit(FakeData.deposits.toList()
      .map {
        it.copy(
          balance = it.balance * Math.random() + 10
        )
      }
    )
  }

  override fun getTerm(depositId: Long): Flow<DepositTerm> = flow {
    val term = FakeData.termsForDeposits[depositId] ?: throw IllegalArgumentException()
    val r = (Math.random() * 6) + 6
    emit(term.copy(rate = r.toFloat()))
  }
}