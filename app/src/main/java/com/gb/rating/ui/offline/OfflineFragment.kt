package com.gb.rating.ui.offline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gb.rating.R
import com.gb.rating.ui.offline.OfflineViewModel


class OfflineFragment : Fragment() {

    private lateinit var offlineViewModel: OfflineViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        offlineViewModel =
            ViewModelProviders.of(this).get(OfflineViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_offline, container, false)
        val textView: TextView = root.findViewById(R.id.text_offline)
        offlineViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}