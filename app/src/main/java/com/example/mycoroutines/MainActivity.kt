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
        coroutineExample()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // 1つ目も2つ目もキャンセル
    }

    override fun onPause() {
        super.onPause()
        coroutineScope.cancel() // 1つ目も2つ目もキャンセル
    }

}
