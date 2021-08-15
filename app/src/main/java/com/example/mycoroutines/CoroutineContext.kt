package com.example.mycoroutines

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

fun completableJob(context: CompletableJob) {

    val scope1 = CoroutineScope(context)
    scope1.launch {
        println("start1")
        delay(1000L)
        println("end1")
    }

    val scope2 = CoroutineScope(context)
    scope2.launch {
        println("start2")
        delay(1000L)
        println("end2")
    }

    Thread.sleep(500L)
    // このcontextが使用されているCoroutineを全てキャンセル
    context.cancel()
    Thread.sleep(2000L)
}

fun emptyContext(context: EmptyCoroutineContext) {

    val scope1 = CoroutineScope(context)
    scope1.launch {
        println("start1")
        delay(1000L)
        println("end1")
    }

    val scope2 = CoroutineScope(context)
    scope2.launch {
        println("start2")
        delay(1000L)
        println("end2")
    }

    Thread.sleep(500L)
    // context.cancel()はjobを指定しないとキャンセルされない
    // scope単位でのキャンセルは可能 scope1.cancel()
    context.cancel()
    Thread.sleep(2000L)
}

