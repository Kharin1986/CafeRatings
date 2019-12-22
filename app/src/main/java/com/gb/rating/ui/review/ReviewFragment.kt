package com.gb.rating.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gb.rating.R

class ReviewFragment : Fragment() {

    private lateinit var reviewViewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewViewModel =
            ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_review, container, false)
        val textView: TextView = root.findViewById(R.id.text_review)
        reviewViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}