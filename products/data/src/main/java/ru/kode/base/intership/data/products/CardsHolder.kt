package ru.kode.base.intership.data.products

import kotlinx.coroutines.flow.MutableStateFlow
import ru.kode.base.internship.domain.Card

internal object CardsHolder {
  internal val cache: MutableMap<Long, MutableStateFlow<Card>> = hashMapOf()

}