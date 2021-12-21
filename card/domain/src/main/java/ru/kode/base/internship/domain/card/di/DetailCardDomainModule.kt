package ru.kode.base.internship.domain.card.di

import ru.kode.base.internship.domain.card.detailusecase.DetailUseCase
import ru.kode.base.internship.domain.card.detailusecase.DetailUseCaseImpl
import toothpick.config.Module

class DetailCardDomainModule : Module() {
  init {
    bind(DetailUseCase::class.java)
      .to(DetailUseCaseImpl::class.java)
      .singletonInScope()
  }
}