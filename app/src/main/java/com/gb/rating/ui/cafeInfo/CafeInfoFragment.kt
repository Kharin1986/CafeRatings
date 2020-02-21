package com.gb.rating.ui.cafeInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gb.rating.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CafeInfoFragment : Fragment() {

    companion object {
        fun newInstance() = CafeInfoFragment()
    }

    private val model: CafeInfoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cafe_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(CafeInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
