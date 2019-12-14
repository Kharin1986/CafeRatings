package com.gb.rating.ui.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gb.rating.R

class SaveListFragment : Fragment() {

    private lateinit var saveListViewModel: SaveListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        saveListViewModel =
            ViewModelProviders.of(this).get(SaveListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_save_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_share)
        saveListViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}