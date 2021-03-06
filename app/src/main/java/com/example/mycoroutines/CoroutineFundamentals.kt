package com.example.mycoroutines

import android.util.Log
import kotlinx.coroutines.*

//    runBlockingは新しいCoroutineScopeを作成し、その中で起動された全てのCoroutineが完了するまで処理をブロックします
fun coroutineExample() {
    runBlocking {

        val job1 = launch {
            try {
                println("11")
                delay(1000L)
                println("22")
                delay(1000L)
                println("job1 end")
            } catch (e: CancellationException) {
                // delay等のキャンセル可能な中断関数はCancellationExceptionを吐きます
                println("job1 $e")
            }

        }
        val job2 = launch {
            println("111")
            delay(1000L)
            println("222")
            delay(1000L)
            println("job2 end")
        }

        val differed1 = async{
            fetchData()
        }

        val differed2 = async {
            fetchData()
        }


        kotlin.run {
            // 複数同時にlaunchすることで、並行に動作させることはできますが、
            // 待ち合わせ時に各々の結果の値を使うことができません。
            println("differed1 + differed2 is ${differed1.await() + differed2.await()}")

        }
        // join をつけない場合またずにall endは実行される
        //println("all end")
        delay(500L)
        job1.cancel() // 特定のJobだけキャンセルできる

        // Thread.sleep(2000L) // 処理が完了するまで待機

        launch {
            try {
                println("try1")
                delay(1000L)
                println("try2")
                delay(1000L)
                throw Exception("error!")
            } catch (e: Throwable) {
                println("exception: $e")
            }
        }
    }
}

suspend fun fetchData(): Int {
    delay(1000L)
    return 3.also{Log.d("fetch", it.toString())}
}
suspend fun fetchData1(): Int {
    delay(1000L)
    return 1
}
suspend fun fetchData2(): Int {
    delay(1000L)
    return 2
}

suspend fun fetchBoth(): Pair<Int, Int> =
    coroutineScope {
        val d1 = async { fetchData1() }
        val d2 = async { fetchData2() }
        d1.await() to d2.await()
    }.also { Log.d("both", it.toString()) }
//    fun job1(scope: CoroutineScope) = scope.launch {
//        println("11")
//        delay(1000L)
//        println("22")
//        delay(1000L)
//        println("job1 end")
//    }
//    fun job2(scope: CoroutineScope) = scope.launch {
//        println("111")
//        delay(1000L)
//        println("222")
//        delay(1000L)
//        println("job2 end")
//    }