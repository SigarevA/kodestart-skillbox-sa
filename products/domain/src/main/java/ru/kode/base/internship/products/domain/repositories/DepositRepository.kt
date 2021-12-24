package ru.kode.base.internship.products.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit

interface DepositRepository {
  val deposits: Flow<List<GeneralDeposit>>
  suspend fun load(isRefresh: Boolean)
  suspend fun getTerm(depositId: Long, isRefresh: Boolean): Flow<DepositTerm>
}