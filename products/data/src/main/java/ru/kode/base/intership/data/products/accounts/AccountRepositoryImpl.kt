package ru.kode.base.intership.data.products.accounts

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import ru.kode.base.internship.products.domain.models.GeneralAccount
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.products.data.ProductsDataBase
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val db: ProductsDataBase,
) : AccountRepository {

  private val queries = db.accountEntityQueries

  override val accounts: Flow<List<GeneralAccount>>
    get() = queries.getAllAccount().asFlow()
      .mapToList().map {
        it.map {
          GeneralAccount(
            it.id,
            it.numberAccount,
            it.balance,
            it.currency,
            it.status,
            db.cardInAccountQueries.getAllAccount(it.id, mapper = { _, cardId -> cardId }).executeAsList()
          )
        }
      }

  override suspend fun load(isRefresh: Boolean) {
    if (!isRefresh) {
      val accounts = productsAPI.fetchAccounts().accounts
      queries.transaction {
        accounts.forEach { account ->
          queries.insertAccount(
            account.accountId,
            account.number,
            account.balance,
            account.currency,
            account.status
          )
          account.cards.forEach { card ->
            db.cardInAccountQueries.insertCardInAccount(account.accountId, card.cardId)
          }
        }
      }
    } else {
      queries.getAllAccount().asFlow()
        .mapToList()
        .take(1)
        .collect {
          it.forEach {
            queries.transaction {
              queries.updateBalance((Math.random() * 1_000_000) + 500_00, it.id)
            }
          }
        }
    }

  }
}