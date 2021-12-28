package ru.kode.base.internship.products.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.domain.Card

typealias CardId = Long

interface CardRepository {
  fun cardDetails(id: CardId): Flow<Card>
}