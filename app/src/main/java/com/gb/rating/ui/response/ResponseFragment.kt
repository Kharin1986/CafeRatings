package com.gb.rating.ui.response

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gb.rating.R

class ResponseFragment : Fragment() {

    private lateinit var responseViewModel: ResponseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        responseViewModel =
            ViewModelProviders.of(this).get(ResponseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_response, container, false)
        val textView: TextView = root.findViewById(R.id.text_response)
        responseViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}