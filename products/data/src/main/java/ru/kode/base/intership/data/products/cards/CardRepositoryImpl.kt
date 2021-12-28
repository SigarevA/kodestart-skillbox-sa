package ru.kode.base.intership.data.products.cards

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

  override fun cardDetails(id: Long): Flow<Card> {
    return queries.getCardById(id).asFlow().mapToOneOrNull()
      .onEach {
        if (it == null)
          loadDetailCard(id)
      }
      .filter { it != null }
      .map { it!!.toDomainCard() }
  }

  override fun getCardDetails(id: Long): Flow<Card> {
    return queries.getCardById(id).asFlow().mapToOne().map {
      it.toDomainCard()
    }
  }

  override suspend fun loadDetailCard(id: Long) {
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

  override suspend fun updateCardName(cardId: Long, newName: String) {
    queries.updateName(newName, cardId)
  }
}