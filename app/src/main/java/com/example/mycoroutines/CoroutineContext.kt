package com.example.mycoroutines

import android.util.Log
import android.view.View
import android.widget.TextView
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

//CoroutineContextは+を使うことで合成することができます。
fun compositeContext(view: TextView?) {
    val context = Dispatchers.Main + Job()
    val scope = CoroutineScope(context)
    scope.launch {
        try {
            // UIスレッドで実行
            delay(1000L)
            println("Hello, World!")
            view?.text = "executed on UI Thread" // 監視されてないから変わらない
        } catch (e: Throwable) {
            e.message?.let { Log.e("compositeContext", it) }
        }

    }
    //context.cancel() // キャンセルもできる
}

