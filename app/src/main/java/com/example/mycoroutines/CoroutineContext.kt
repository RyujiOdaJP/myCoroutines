package com.example.mycoroutines

import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*
import javax.security.auth.callback.Callback
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

fun thread() {
    /* 同一Coroutine内であれば、try-catchでエラーハンドリングできることを説明してきました。
    CoroutineExceptionHandlerは、そこでcatchされなかったエラーを処理することができます。*/
    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("catch: $e")
    }

    /*これはJVMでのみで使えるオプションで、IOタスク用のスレッド群から一つ選ばれて実行されます。
    このスレッド群はDispatchers.Defaultと一部共有されています。そのため、
    Thread.currentThread().nameはDefaultDispatcherと出力されます。*/
    val context = Dispatchers.IO

    val scope = CoroutineScope(context)
    println("thread1: ${Thread.currentThread().name}")

    scope.launch(exceptionHandler) { // rootにはexceptionHandler指定できる
        println("thread2: ${Thread.currentThread().name}")

        /*
        * 下記のExceptionがThrowされる
        * 2021-08-18 10:04:47.175 17902-17927/com.example.mycoroutines I/System.out: catch: java.lang.Exception: error
        * */
        throw Exception("error")

        /*
        * 2021-08-18 10:01:39.261 17773-17798/com.example.mycoroutines E/AndroidRuntime: FATAL EXCEPTION: DefaultDispatcher-worker-1
        * Process: com.example.mycoroutines, PID: 17773
        * java.lang.Exception: error
        * */
//        withContext(exceptionHandler) { // ここでexceptionHandler指定はできない
//            delay(1000L)
//            throw Exception("error")
//        }
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

fun errorHandling() {
    val context = Job()
    val scope = CoroutineScope(context)
    scope.launch {
        try {
            /*
            * coroutineScopeやwithContextなどsuspend functionで囲うと、内部で起動した全てのCoroutinesの実行完了を待ってくれるため、
            * try-catchでエラーを取得することができます。
            * A suspending function is simply a function that can be paused and resumed at a later time.
            *  They can execute a long running operation and wait for it to complete without blocking.
            * */
            withContext(Dispatchers.Default) {
                launch {
                    delay(1000L)
                    throw Exception("error")
                }
            }
        } catch (e: Throwable) {
            println("catch: $e")
        }
    }
    Thread.sleep(2000L)
}

fun getSomething(str: String, callback: (str: String) -> Int) =
    str.takeIf { it.toIntOrNull() != null }?.let {
        callback(it) * callback(it)
    }

fun async() {
    val context = Job()
    val scope = CoroutineScope(context)
    val differed = scope.async {
        try {
            delay(1000)
            throw Exception("error")
        } catch (e: Throwable) {
            e.message
        }
    }
    scope.launch {
        println("result: ${differed.await()}")
    }
    Thread.sleep(2000L)
}

/**
 * 渡したsuspend functionをretryする
 *
 * @retries 最大試行回数
 * @intervalMills 施行間隔
 *
 * @return 最大試行回数を超過した場合はnullを返す
 */
suspend fun <T> retryOrNull(
    retries: Int,
    intervalMills: Long = 1000L,
    block: suspend () -> T
): T? {
    repeat(retries) {
        try {
            return block()
        } catch (e: Throwable) {
            delay(intervalMills)
        } catch (e: CancellationException) {
            // キャンセル時は再スローする
            throw e
        }
    }
    return null
}

suspend fun <T> retryUntilTrueOrNull(
    retries: Int,
    predicate: suspend (cause: Throwable) -> Boolean = { true },
    block: suspend () -> T
): T? {
    repeat(retries) {
        try {
            return block()
        } catch (e: Throwable) {
            if(!predicate(e)) return null
        } catch (e: CancellationException) {
            // キャンセル時は再スローする
            throw e
        }
    }
    return null
}