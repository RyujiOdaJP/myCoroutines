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
            fun job1() = launch {
                println("11")
                delay(1000L)
                println("22")
                delay(1000L)
                println("job1 end")
            }
            fun job2() = launch {
                println("111")
                delay(1000L)
                println("222")
                delay(1000L)
                println("job2 end")
            }
            kotlin.run {
                job1()
                job2()
            }.join()
            // join をつけない場合またずにall endは実行される
            println("all end")

            // Thread.sleep(2000L) // 処理が完了するまで待機
        }
    }
}
