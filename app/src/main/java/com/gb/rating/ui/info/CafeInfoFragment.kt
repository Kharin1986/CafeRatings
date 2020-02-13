package com.gb.rating.ui.info

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gb.rating.R
import com.gb.rating.ui.home.HomeViewModel

class CafeInfoFragment : Fragment() {

    private lateinit var infoViewModel: CafeInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        infoViewModel =
            ViewModelProviders.of(this).get(CafeInfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cafe_info, container, false)
        return root
    }
}
