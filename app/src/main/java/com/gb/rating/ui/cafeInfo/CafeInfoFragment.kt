package com.gb.rating.ui.cafeInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.gb.rating.R
import com.gb.rating.ui.ViewModelMain
import kotlinx.android.synthetic.main.fragment_cafe_info.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CafeInfoFragment : Fragment() {
    private lateinit var adapter: CafeInfoAdapter

    private val mViewModel: CafeInfoViewModel by viewModel()
    private val activityViewModel: ViewModelMain by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cafe_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        adapter = CafeInfoAdapter()

        rv_cafe_info_list?.layoutManager = LinearLayoutManager(activity)
        rv_cafe_info_list?.adapter = adapter

        mViewModel.viewState.observe(this, Observer { })
        activityViewModel.cafeReviewList().observe(this, Observer {
            it?.let {
                adapter.refreshList(it)
            }
        })
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        //viewModel = ViewModelProvider(this).get(CafeInfoViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
