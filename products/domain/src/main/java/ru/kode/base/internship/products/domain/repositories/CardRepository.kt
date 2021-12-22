package ru.kode.base.internship.products.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.domain.Card

typealias CardId = Long

interface CardRepository {
  suspend fun cardDetails(id: Long, isRefresh : Boolean): Flow<Card>
}