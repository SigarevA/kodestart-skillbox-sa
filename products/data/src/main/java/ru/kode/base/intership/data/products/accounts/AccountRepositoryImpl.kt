package ru.kode.base.intership.data.products.accounts

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import ru.kode.base.internship.products.domain.models.GeneralAccount
import ru.kode.base.internship.products.domain.repositories.AccountRepository
import ru.kode.base.intership.data.network.ProductsAPI
import ru.kode.base.intership.products.data.ProductsDataBase
import timber.log.Timber
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
  private val productsAPI: ProductsAPI,
  private val db: ProductsDataBase,
) : AccountRepository {

  private val queries = db.accountEntityQueries

  private val _accounts: MutableSharedFlow<List<GeneralAccount>> = MutableSharedFlow()
  private val asd = queries.getAllAccount().asFlow()
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

  override val accounts: Flow<List<GeneralAccount>>
    get() = _accounts.asSharedFlow()

  override suspend fun load(isRefresh: Boolean) {
    if (isRefresh) {
      Timber.d("insert accounts load")
      val accounts = productsAPI.fetchAccounts().accounts
      queries.transaction {
        Timber.d("insert accounts")
        Timber.d("insert accounts size : ${accounts.size}")
        accounts.forEach { account ->
          Timber.d("insert account : ${account}")
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
    }
    asd.collect {
      Timber.d("collect accounts")
      Timber.d("check size ${it.size}")
      _accounts.emit(it)
    }
  }
}