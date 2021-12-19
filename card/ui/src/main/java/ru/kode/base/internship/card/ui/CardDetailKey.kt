package ru.kode.base.internship.card.ui

import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import ru.kode.base.core.model.ComponentConfig
import ru.kode.base.core.model.ScreenKey
import ru.kode.base.ui.core.util.ToothpickScreenBindings

@Parcelize
data class CardDetailKey(val cardId: Long) : ScreenKey() {
  @Suppress("INAPPLICABLE_IGNORED_ON_PARCEL")
  @IgnoredOnParcel
  override val componentConfig = ComponentConfig(
    presenterClass = CardDetailPresenter::class.java,
    controllerClass = CardDetailController::class.java,
    screenBindings = ToothpickScreenBindings {
      bind(CardParams::class.java).toInstance(CardParams(cardId))
    }
  )
}

internal data class CardParams(val cardId : Long)