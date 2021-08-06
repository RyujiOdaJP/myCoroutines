package com.example.mycoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main()
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
        val coroutineScope = CoroutineScope(EmptyCoroutineContext)
        coroutineScope.launch {
            println("11")
            delay(1000L)
            println("22")
        }
        coroutineScope.launch {
            println("33")
        }
        Thread.sleep(2000L) // 処理が完了するまで待機
    }
}
