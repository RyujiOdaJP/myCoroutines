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

            launch {
                // 子CoroutineもUIスレッド(contextは伝搬する)
                delay(1000L)
                println("child")
            }
            withContext(Dispatchers.Default) {
                // contextを途中で変える（BackgroundThread）
                delay(1000L)
                println("child BackgroundThread")
            }
            // UIスレッドで実行
            delay(1000L)
            println("parent!")
            view?.text = "executed on UI Thread" // 監視されてないから変わらない
        } catch (e: Throwable) {
            e.message?.let { Log.e("compositeContext", it) }
        }

    }
    //context.cancel() // キャンセルもできる
}
fun childHasNewJob() {
    val context = Job()
    val scope = CoroutineScope(context)
    scope.launch(Job()) { // 新しいJobを持った子CoroutineScope
        delay(1000L)
        println("1")
    }
    scope.launch { // 親のJobを引き継いだCoroutineScope
        delay(1000L)
        println("2")
    }
    // 親のJobを引き継いだCoroutineScopeのみキャンセル
    context.cancel()
    scope.cancel()
    Thread.sleep(2000L)
}

fun nonCancelable() {
    val context = Job()
    val scope = CoroutineScope(context)
    scope.launch(NonCancellable) {  // キャンセル不可能なCoroutineScope
        delay(1000L)
        println("1")
    }
    scope.launch { // 親のJobを引き継いだCoroutineScope
        delay(1000L)
        println("2")
    }
    // NonCancellableはキャンセルされない
    context.cancel()
    scope.cancel()
    Thread.sleep(2000L)
}

fun thread(){
    val context = Dispatchers.Default
    val scope = CoroutineScope(context)
    println("thread1: ${Thread.currentThread().name}")

    scope.launch {
        println("thread2: ${Thread.currentThread().name}")
    }

    val context2 = Dispatchers.Main.immediate
    val scope2 = CoroutineScope(context2)

    println("1")
    scope2.launch {
        println("2")
        delay(100L)
        println("3")
    }
    println("4")
}
