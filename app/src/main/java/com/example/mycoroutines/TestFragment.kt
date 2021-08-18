package com.example.mycoroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mycoroutines.databinding.FragmentTestBinding

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
        errorHandling()
    }.root

}