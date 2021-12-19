package ru.kode.base.internship.card.ui

import kotlinx.coroutines.launch
import ru.dimsuz.unicorn.coroutines.MachineDsl
import ru.kode.base.core.coroutine.BasePresenter
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.card.detailusecase.DetailUseCase
import javax.inject.Inject

internal class CardDetailPresenter @Inject constructor(
  private val params: CardParams,
  private val detailUseCase: DetailUseCase,
) :
  BasePresenter<CardDetailScreen.ViewState, CardDetailScreen.ViewIntents, Unit>() {

  override fun MachineDsl<CardDetailScreen.ViewState, Unit>.buildMachine() {
    initial = CardDetailScreen.ViewState() to {
      launch {
        detailUseCase.findCardDetail(params.cardId)
      }
    }

    onEach(detailUseCase.updateCardNameState) {
      transitionTo { state, updateState ->
        when (updateState) {
          is LceState.Error -> state.copy(
            updateCardNameState = updateState,
            updateStatus = CardDetailScreen.UpdateStatus.Failure,
            newName = null
          )
          LceState.Content -> if (state.card != null)
            state.copy(
              card = state.card.copy(name = state.newName!!),
              updateCardNameState = updateState,
              updateStatus = CardDetailScreen.UpdateStatus.Success
            )
          else
            state.copy(
              updateCardNameState = updateState,
              newName = null
            )
          else -> state.copy(updateCardNameState = updateState)
        }
      }
    }

    onEach(detailUseCase.card) {
      transitionTo { state, card ->
        state.copy(card = card)
      }
    }

    onEach(detailUseCase.cardDetailState) {
      transitionTo { state, payload ->
        state.copy(detailCardState = payload)
      }
    }

    onEach(intent(CardDetailScreen.ViewIntents::openDialog)) {
      transitionTo { state, _ ->
        state.copy(isOpenDialog = true)
      }
    }

    onEach(intent(CardDetailScreen.ViewIntents::closeDialog)) {
      transitionTo { state, _ ->
        state.copy(isOpenDialog = false, newName = null)
      }
    }

    onEach(intent(CardDetailScreen.ViewIntents::changeCardName)) {
      transitionTo { state, payload ->
        if (state.updateCardNameState != LceState.Loading)
          state.copy(newName = payload)
        else
          state
      }
    }

    onEach(intent(CardDetailScreen.ViewIntents::clickChangeName)) {
      transitionTo { state, _ ->
        if (state.newName != null && state.card != null && state.updateCardNameState != LceState.Loading)
          state.copy(isOpenDialog = false)
        else
          state
      }
      action { _, state, _ ->
        if (state.newName != null && state.card != null && state.updateCardNameState != LceState.Loading) {
          executeAsync {
            detailUseCase.updateCardName(state.card.id, state.newName)
          }
        }
      }
    }

    onEach(intent(CardDetailScreen.ViewIntents::dismiss)) {
      transitionTo { state, _ ->
        state.copy(updateStatus = null)
      }
    }
  }
}