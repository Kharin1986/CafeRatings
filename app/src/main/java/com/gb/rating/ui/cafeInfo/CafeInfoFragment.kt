package com.gb.rating.ui.cafeInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.gb.rating.R

class CafeInfoFragment : Fragment() {

    companion object {
        fun newInstance() = CafeInfoFragment()
    }

    private lateinit var viewModel: CafeInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cafe_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CafeInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
