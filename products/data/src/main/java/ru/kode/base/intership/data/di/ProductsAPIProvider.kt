package ru.kode.base.intership.data.di

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.kode.base.internship.core.BuildConfig
import ru.kode.base.internship.core.data.network.adapter.LocalDateJsonAdapter
import ru.kode.base.internship.core.data.network.adapter.LocalDateTimeJsonAdapter
import ru.kode.base.internship.core.data.network.adapter.LocalTimeJsonAdapter
import ru.kode.base.intership.data.network.ProductsAPI
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

internal class ProductsAPIProvider @Inject constructor(
  private val retrofit: Retrofit,
) : Provider<ProductsAPI> {
  override fun get(): ProductsAPI = getRetrofit(createMoshi(), getClient()).create(ProductsAPI::class.java)
}

private fun createMoshi(): Moshi {
  return Moshi.Builder()
    .add(LocalDateJsonAdapter(LOCAL_DATE_SERVER_FORMAT))
    .add(LocalDateTimeJsonAdapter(LOCAL_DATE_TIME_SERVER_FORMAT))
    .add(LocalTimeJsonAdapter(LOCAL_TIME_SERVER_FORMAT))
    .build()
}

private fun getClient(): OkHttpClient {
  val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("OkHttp"); Timber.d(message) }
  loggingInterceptor.level = HTTP_LOG_LEVEL
  return OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
    .readTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
    .writeTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
    .build()
}

private fun getRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit {
  return Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    //.addCallAdapterFactory(resultCallAdapterFactory)
    .client(client)
    .baseUrl("$BASE_URL/").build()
}

internal const val BASE_URL = "https://stoplight.io/mocks/kode-education/kode-bank/27774161"
internal const val HTTP_CONNECT_TIMEOUT = 60_000L
internal val HTTP_LOG_LEVEL =
  if (BuildConfig.RELEASE) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.BODY
internal val LOCAL_DATE_SERVER_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE
internal val LOCAL_DATE_TIME_SERVER_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME
internal val LOCAL_TIME_SERVER_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME