package ru.kode.base.intership.data.products.cards

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.domain.card.repositories.DetailCardRepository
import ru.kode.base.internship.products.domain.repositories.CardId
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.intership.data.products.FakeData
import ru.kode.base.intership.data.products.mappers.toDomainCard
import timber.log.Timber
import javax.inject.Inject

internal class CardRepositoryImpl @Inject constructor() : CardRepository, DetailCardRepository {

  private val cache: MutableMap<Long, MutableStateFlow<Card>> = hashMapOf()

  override fun cardDetails(id: CardId): Flow<Card> {
    return cache[id]?.asStateFlow() ?: run {
      val card = FakeData.cards.find { it.id == id } ?: throw IllegalArgumentException()
      MutableStateFlow(card.toDomainCard()).also {
        cache[id] = it
      }.asStateFlow()
    }
  }

  override fun getCardDetails(id: Long): Flow<Card> {
    return cache[id]?.asStateFlow() ?: run {
      val card = FakeData.cards.find { it.id == id } ?: throw IllegalArgumentException()
      MutableStateFlow(card.toDomainCard()).also {
        cache[id] = it
      }.asStateFlow()
    }
  }

  override suspend fun updateCardName(cardId: Long, newName: String) {
    FakeData.cards.forEachIndexed { i, card ->
      if (card.id == cardId) {
        FakeData.cards[i] = card.copy(name = newName)
        cache[cardId]?.let {
          Timber.d("new value : ${FakeData.cards[i].toDomainCard()}")
          it.emit(FakeData.cards[i].toDomainCard())
        }
        return
      }
    }
    throw IllegalArgumentException()
  }
}