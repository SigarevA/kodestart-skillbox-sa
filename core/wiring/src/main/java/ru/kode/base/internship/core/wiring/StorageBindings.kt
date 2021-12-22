package ru.kode.base.internship.core.wiring

import com.squareup.sqldelight.db.SqlDriver
import ru.kode.base.core.util.ToothpickModuleBindings
import toothpick.config.Module

object StorageBindings : ToothpickModuleBindings {
  override fun bindInto(module: Module) {
    module.bind(SqlDriver::class.java)
      .toProvider(InMemoryDatabaseDriverProvider::class.java)
      .providesSingletonInScope()
  }
}