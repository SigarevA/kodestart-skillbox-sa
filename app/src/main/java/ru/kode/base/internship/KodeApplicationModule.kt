package ru.kode.base.internship

import android.content.Context
import coil.ImageLoader
import okhttp3.OkHttpClient
import ru.kode.base.core.annotations.ApplicationContext
import ru.kode.base.internship.core.wiring.StorageBindings
import ru.kode.base.internship.core.data.di.DataBindings
import toothpick.config.Module
import javax.inject.Inject
import javax.inject.Provider

internal class KodeApplicationModule(application: KodeApplication) : Module() {
  init {
    bind(Context::class.java).withName(ApplicationContext::class.java).toInstance(application)
    bind(KodeApplication::class.java).toInstance(application)
    bind(ImageLoader::class.java).toProvider(ImageLoaderProvider::class.java).providesSingletonInScope()
    StorageBindings.bindInto(this)
    DataBindings.bindInto(this)
  }
}

private class ImageLoaderProvider @Inject constructor(
  @ApplicationContext private val context: Context,
  private val okHttpClient: OkHttpClient,
) : Provider<ImageLoader> {
  override fun get(): ImageLoader {
    return ImageLoader.Builder(context)
      .okHttpClient(okHttpClient)
      .build()
  }
}
