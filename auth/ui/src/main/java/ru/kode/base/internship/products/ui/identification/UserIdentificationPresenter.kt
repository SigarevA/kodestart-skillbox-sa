package ru.kode.base.internship.products.ui.identification

import ru.dimsuz.unicorn.coroutines.MachineDsl
import ru.kode.base.internship.products.ui.identification.UserIdentificationScreen.ViewState
import ru.kode.base.internship.products.ui.identification.UserIdentificationScreen.ViewIntents
import ru.kode.base.core.coroutine.BasePresenter
import ru.kode.base.internship.auth.domain.AuthUseCase
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.routing.AppFlow
import ru.kode.base.internship.products.ui.identification.UserIdentificationScreen.ErrorMessage
import javax.inject.Inject

internal class UserIdentificationPresenter @Inject constructor(
  private val authUseCase: AuthUseCase,
  private val coordinator: AppFlow.Coordinator,
) : BasePresenter<ViewState, ViewIntents, Unit>() {

  override fun MachineDsl<ViewState, Unit>.buildMachine() {
    initial = ViewState() to null

    onEach(intent(ViewIntents::changeText)) {
      transitionTo { state, text ->
        state.copy(enteredPhoneNumber = text.take(REQUIRED_PHONE_LENGTH))
      }
    }

    onEach(intent(ViewIntents::dismissError)) {
      transitionTo { state, _ ->
        val identificationLceState = if (state.identificationLceState is LceState.Error) {
          LceState.None
        } else {
          state.identificationLceState
        }
        state.copy(
          identificationLceState = identificationLceState,
          errorMessage = null,
        )
      }
    }

    onEach(intent(ViewIntents::login)) {
      transitionTo { state, _ ->
        val validationError = if (state.enteredPhoneNumber.length != REQUIRED_PHONE_LENGTH) {
          ErrorMessage.ValidationError.IncorrectPhoneNumber
        } else {
          state.errorMessage
        }
        state.copy(
          errorMessage = validationError,
        )
      }
      action { _, newState, _ ->
        if (newState.errorMessage == null) {
          executeAsync {
            authUseCase.identifyUser(newState.enteredPhoneNumber)
          }
        }
      }
    }

    onEach(authUseCase.userIdentificationState) {
      transitionTo { state, lceState ->
        state.copy(
          identificationLceState = lceState,
          errorMessage = if(lceState is LceState.Error) ErrorMessage.IdentificationError else state.errorMessage
        )
      }
      action { _, newState, _ ->
        if (newState.identificationLceState == LceState.Content) {
          coordinator.handleEvent(AppFlow.Event.LoginRequested)
        }
      }
    }
  }
}

private const val REQUIRED_PHONE_LENGTH = 10
