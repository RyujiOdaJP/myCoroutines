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
//        runBlocking {
//            // 複数回launchすることで、それらの処理は並行して動作します。
//            launch {
//                println("1")
//                delay(1000L)
//                println("2")
//            }
//            launch {
//                println("3")
//            }
//        }

        coroutineScope.launch {
            println("11")
            delay(1000L)
            println("22")
            delay(1000L)
            println("33")
        }
        coroutineScope.launch {
            println("111")
            delay(1000L)
            println("222")
            delay(1000L)
            println("333")

            //子Coroutine Scope
            launch {
                println("1111")
                delay(1000L)
                println("2222")
                delay(1000L)
                println("3333")
            }
        }

        // Thread.sleep(2000L) // 処理が完了するまで待機
    }
}
