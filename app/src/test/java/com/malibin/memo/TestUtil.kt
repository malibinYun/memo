package com.malibin.memo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.mockito.ArgumentCaptor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.takeValue(): T? {
    var result: T? = null
    val countDownLatch = CountDownLatch(1)
    var observer = Observer<T> {}

    observer = Observer {
        result = it
        countDownLatch.countDown()
        this.removeObserver(observer)
    }
    this.observeForever(observer)
    countDownLatch.await(2000, TimeUnit.MILLISECONDS)
    return result
}

