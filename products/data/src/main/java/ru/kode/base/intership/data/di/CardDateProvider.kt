package ru.kode.base.intership.data.di

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider

class CardDateProvider @Inject constructor() : Provider<SimpleDateFormat> {
  override fun get(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())
}