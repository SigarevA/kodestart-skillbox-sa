package ru.kode.base.internship.domain.card.repositories

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.domain.Card

interface DetailCardRepository {
  suspend fun getCardDetails(id: Long): Flow<Card>
  suspend fun updateCardName(cardId: Long, newName: String)
}