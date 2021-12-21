package ru.kode.base.internship.domain.card.detailusecase

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card

interface DetailUseCase {
  suspend fun findCardDetail(cardId: Long)
  suspend fun updateCardName(cardId: Long, newName: String)

  val updateCardNameState: Flow<LceState>
  val cardDetailState: Flow<LceState>
  val card: Flow<Card>
}