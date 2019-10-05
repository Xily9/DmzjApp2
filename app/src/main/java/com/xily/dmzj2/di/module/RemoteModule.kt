package com.xily.dmzj2.di.module

import com.xily.dmzj2.BuildConfig
import com.xily.dmzj2.data.remote.HttpHelper
import com.xily.dmzj2.data.remote.RetrofitHelper
import com.xily.dmzj2.data.remote.service.DmzjApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Xily on 2019/7/14.
 */
val httpModule = module {
    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    factory<DmzjApiService> {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1/")//随便写一个,不写会报错
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DmzjApiService::class.java)
    }

    factory<HttpHelper> {
        RetrofitHelper(get())
    }
}