package ru.kode.base.internship.domain.card.detailusecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.domain.card.repositories.DetailCardRepository
import javax.inject.Inject

internal class DetailUseCaseImpl @Inject constructor(
  private val detailCardRepository: DetailCardRepository,
  private val scope: CoroutineScope,
) : BaseUseCase<DetailUseCaseImpl.State>(scope, State()),
  DetailUseCase {

  data class State(
    val detailCardState: LceState = LceState.None,
    val updateCardNameState: LceState = LceState.None,
    val card: Card? = null,
  )

  override suspend fun findCardDetail(cardId: Long) {
    setState { copy(detailCardState = LceState.Loading) }
    detailCardRepository
      .getCardDetails(cardId)
      .onEach { data ->
        setState { copy(detailCardState = LceState.Content, card = data) }
      }
      .catch { ex ->
        setState { copy(detailCardState = LceState.Error(ex.message), card = null) }
      }
      .stateIn(scope)
  }

  override suspend fun updateCardName(cardId: Long, newName: String) {
    setState { copy(updateCardNameState = LceState.Loading) }
    try {
      detailCardRepository.updateCardName(cardId, newName)
      setState { copy(updateCardNameState = LceState.Content) }
    } catch (e: Exception) {
      setState { copy(updateCardNameState = LceState.Error(e.message)) }
    }
  }

  override val updateCardNameState: Flow<LceState>
    get() = stateFlow.map { it.updateCardNameState }.distinctUntilChanged()

  override val cardDetailState: Flow<LceState>
    get() = stateFlow.map { it.detailCardState }.distinctUntilChanged()

  override val card: Flow<Card>
    get() = stateFlow.filter { it.card != null }.map { it.card!! }.distinctUntilChanged()
}