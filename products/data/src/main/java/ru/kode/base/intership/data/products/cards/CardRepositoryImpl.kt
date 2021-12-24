package ru.kode.base.intership.data.products.cards

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.domain.card.repositories.DetailCardRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.data.products.CardsHolder
import ru.kode.base.intership.data.products.mappers.toCard
import ru.kode.base.intership.data.products.mappers.toDomainCard
import rukodebaseintershipproductsdata.CardEntityQueries
import timber.log.Timber
import javax.inject.Inject

internal class CardRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val queries: CardEntityQueries,
) : CardRepository, DetailCardRepository {

  private val cache: MutableMap<Long, MutableStateFlow<Card>>
    get() = CardsHolder.cache

  override suspend fun cardDetails(id: Long, isRefresh: Boolean): Flow<Card> {
    Timber.d("call cardDetails with id : ${id}")
    if (!isRefresh) {
      Timber.d("call cardDetails with id : ${id}")
      val card = productsAPI.fetchDetailCard(id, "android-$id").toCard()
      queries.insertCard(
        card.id,
        card.accountId,
        "ds",
        card.paymentSystem,
        card.status,
        card.name,
        card.type,
        card.name
      )
      Timber.d("save call cardDetails with id : ${card}")
    }
    Timber.d("return save call cardDetails with id")
    return queries.getCardById(id).asFlow().mapToOne().map {
      Timber.d("card account")
      it.toDomainCard()
    }
  }


  override suspend fun getCardDetails(id: Long): Flow<Card> {
    return queries.getCardById(id).asFlow().mapToOne().map {
      it.toDomainCard()
    }
  }

  override suspend fun updateCardName(cardId: Long, newName: String) {
    queries.updateName(newName, cardId)
  }
}