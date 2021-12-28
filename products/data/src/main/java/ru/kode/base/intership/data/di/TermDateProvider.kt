package ru.kode.base.intership.data.di

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider

class TermDateProvider @Inject constructor() : Provider<SimpleDateFormat> {
  override fun get(): SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
}