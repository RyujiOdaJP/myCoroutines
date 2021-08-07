package com.example.mycoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // 1つ目も2つ目もキャンセル
    }

    override fun onPause() {
        super.onPause()
        coroutineScope.cancel() // 1つ目も2つ目もキャンセル
    }


    //    runBlockingは新しいCoroutineScopeを作成し、その中で起動された全てのCoroutineが完了するまで処理をブロックします
    fun main() {
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

            kotlin.run {
                job1
                job2
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
}
