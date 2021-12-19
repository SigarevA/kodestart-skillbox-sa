package ru.kode.base.intership.data.products.cards

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.domain.card.repositories.DetailCardRepository
import ru.kode.base.internship.products.domain.repositories.CardId
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.intership.data.products.FakeData
import ru.kode.base.intership.data.products.mappers.toDomainCard

import javax.inject.Inject

internal class CardRepositoryImpl @Inject constructor() : CardRepository, DetailCardRepository {
  override fun cardDetails(id: CardId): Flow<Card> =
    flow {
      emit(FakeData.cards.find { it.id == id } ?: throw IllegalArgumentException())
    }.map {
      it.toDomainCard()
    }

  override fun getCardDetails(id: Long): Flow<Card> = flow {
    emit(FakeData.cards.find { it.id == id } ?: throw IllegalArgumentException())
  }.map {
    it.toDomainCard()
  }

  override suspend fun updateCardName(cardId: Long, newName: String) {
    FakeData.cards.forEachIndexed { i, card ->
      if (card.id == cardId) {
        FakeData.cards[i] = card.copy(name = newName)
        return
      }
    }
    throw IllegalArgumentException()
  }
}