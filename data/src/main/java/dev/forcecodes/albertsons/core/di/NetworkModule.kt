/**
 * Copyright 2024 strongforce1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package dev.forcecodes.albertsons.core.di

import android.content.Context
import android.os.Looper
import androidx.annotation.WorkerThread
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.albertsons.core.api.NetworkStatusProvider
import dev.forcecodes.albertsons.core.api.RandomUserApiService
import dev.forcecodes.albertsons.core.api.interceptor.ConnectivityInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

internal val sharedMoshi =
    Moshi.Builder()
        .build()

@Module(includes = [NetworkStatusModule::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val DEFAULT_TIMEOUT = 15L
    private const val CACHE_SIZE = 10 * 1024 * 1024

    private const val BASE_URL = "https://randomuser.me"

    @Provides
    @Singleton
    internal fun provideCache(
        @ApplicationContext context: Context,
    ): Cache {
        return Cache(context.cacheDir, CACHE_SIZE.toLong())
    }

    @Provides
    @Singleton
    internal fun provideConnectivityInterceptor(networkStatusProvider: NetworkStatusProvider): ConnectivityInterceptor =
        ConnectivityInterceptor(networkStatusProvider)

    @Provides
    @Singleton
    internal fun providesRetrofitBuilder(okHttpClient: Lazy<OkHttpClient>): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(sharedMoshi))
            .delegatingCallFactory(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun providesOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @WorkerThread
    @Singleton
    internal fun providesOkHttpCallFactory(
        connectivityInterceptor: ConnectivityInterceptor,
        okHttpLoggingInterceptor: HttpLoggingInterceptor,
        cache: Cache,
    ): OkHttpClient {
//        return checkMainThread {
//
//        }
        return OkHttpClient.Builder().apply {
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(okHttpLoggingInterceptor)
            addInterceptor(connectivityInterceptor)
            cache(cache)
        }.build()
    }

    @Provides
    @Singleton
    fun providesRandomUserApiService(retrofit: Retrofit): RandomUserApiService = retrofit.create()
}

fun <T> checkMainThread(block: () -> T): T =
    if (Looper.myLooper() == Looper.getMainLooper()) {
//        throw IllegalStateException("Object initialized on main thread.")
        block()
    } else {
        block()
    }

// We use callFactory lambda here with dagger.Lazy<Call.Factory>
// to prevent initializing OkHttp on the main thread.
@Suppress("NOTHING_TO_INLINE")
inline fun Retrofit.Builder.delegatingCallFactory(delegate: Lazy<OkHttpClient>): Retrofit.Builder =
    callFactory {
        delegate.get().newCall(it)
    }
