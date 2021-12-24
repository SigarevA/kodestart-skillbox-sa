package ru.kode.base.intership.data.products.cards

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.domain.card.repositories.DetailCardRepository
import ru.kode.base.internship.products.domain.repositories.CardRepository
import ru.kode.base.intership.data.di.ProductsDataModule.Companion.SIMPLE_DATE_CARD
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.data.products.mappers.toDomainCard
import rukodebaseintershipproductsdata.CardEntityQueries
import java.text.DateFormat
import javax.inject.Inject
import javax.inject.Named

internal class CardRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val queries: CardEntityQueries,
  @Named(SIMPLE_DATE_CARD) val dateFormat: DateFormat,
) : CardRepository, DetailCardRepository {

  override suspend fun cardDetails(id: Long, isRefresh: Boolean): Flow<Card> {
    if (!isRefresh) {
      val card = productsAPI.fetchDetailCard(id, "android-$id")
      queries.insertCard(
        card.id,
        card.accountId,
        dateFormat.parse(card.expiredAt)?.time ?: throw IllegalArgumentException(),
        "Visa",
        card.status,
        card.name,
        "physical",
        card.name
      )
    }
    return queries.getCardById(id).asFlow().mapToOne().map {
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