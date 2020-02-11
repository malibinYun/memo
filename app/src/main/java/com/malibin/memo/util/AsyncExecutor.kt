package com.malibin.memo.util

import android.annotation.SuppressLint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
fun <V> executeAsyncInIoThread(
    callable: () -> V,
    error: (t: Throwable) -> Unit,
    callback: ((values: V) -> Unit)
) {
    Single.fromCallable(callable)
        .subscribeOn(Schedulers.io())
        .subscribe(
            { callback.invoke(it) },
            { error.invoke(it) })
}

@SuppressLint("CheckResult")
fun <V> executeAsyncInMainThread(
    callable: () -> V,
    error: (t: Throwable) -> Unit,
    callback: ((values: V) -> Unit)
) {
    Single.fromCallable(callable)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(
            { callback.invoke(it) },
            { error.invoke(it) })
}