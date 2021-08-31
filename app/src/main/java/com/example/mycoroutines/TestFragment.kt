package com.example.mycoroutines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mycoroutines.databinding.FragmentTestBinding
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class TestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestBinding.inflate(inflater, container, false).apply {
        //compositeContext(view?.findViewById(R.id.textView))
//        childHasNewJob()
//        nonCancelable()
//        thread()
//        errorHandling()
//        Log.d(getSomething("2") {
//            it.toInt()
//        }.toString(), "8888")
//        async()
lifecycleScope.launch {
    retryOrNull(10, 1000L){
        fetchData().also { Log.d("retryOrNull", it.toString()) }
    }
}
lifecycleScope.launch {
    retryUntilTrueOrNull(10, {
        return@retryUntilTrueOrNull it.cause != NullPointerException()
    },
        {fetchData()}
    )
}
    }.root
}