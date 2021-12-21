package ru.kode.base.internship.card.ui

import androidx.compose.runtime.Immutable
import ru.kode.base.core.BaseViewIntents
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card

internal object CardDetailScreen {
  class ViewIntents : BaseViewIntents() {
    val openDialog = intent(name = "openDialog")
    val closeDialog = intent(name = "closeDialog")
    val changeCardName = intent<String>(name = "changeCardName")
    val clickChangeName = intent(name = "clickChangeName")
    val dismiss = intent(name = "dismiss")
  }

  @Immutable
  data class ViewState(
    val detailCardState: LceState = LceState.None,
    val card: Card? = null,
    val isOpenDialog: Boolean = false,
    val newName: String? = null,
    val updateCardNameState: LceState = LceState.None,
    val updateStatus: UpdateStatus? = null,
  )

  enum class UpdateStatus {
    Success, Failure
  }
}