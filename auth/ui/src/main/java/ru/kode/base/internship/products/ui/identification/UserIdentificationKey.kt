package ru.kode.base.internship.products.ui.identification

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.kode.base.core.model.ComponentConfig
import ru.kode.base.core.model.ScreenKey

@Parcelize
object UserIdentificationKey : ScreenKey() {
  // See NOTE_IGNORED_ON_PARCEL_AND_OBJECT
  @Suppress("INAPPLICABLE_IGNORED_ON_PARCEL")
  @IgnoredOnParcel
  override val componentConfig = ComponentConfig(
    presenterClass = UserIdentificationPresenter::class.java,
    controllerClass = UserIdentificationController::class.java,
  )
}
