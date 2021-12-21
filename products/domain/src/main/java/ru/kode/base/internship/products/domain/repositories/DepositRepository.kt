package ru.kode.base.internship.products.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.models.DepositTerm
import ru.kode.base.internship.products.domain.models.GeneralDeposit

interface DepositRepository {
  val deposits: Flow<List<GeneralDeposit>>
  suspend fun load()
  fun getTerm(depositId: Long): Flow<DepositTerm>
}