package com.gb.rating.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import android.R
import com.gb.rating.ui.ViewModelMain


class ListFragment : Fragment() {
    var activityViewModel : ViewModelMain? = null
    var mViewModel : ListViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.gb.rating.R.layout.fragment_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        //инициализируем адаптер и присваеваем его списку
        val adapter = ListAdapter()
        cafeListRecycler_FragmentList?.layoutManager = LinearLayoutManager(activity)
        cafeListRecycler_FragmentList?.adapter = adapter

        mViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        activity?.let { fragmentActivity ->  activityViewModel = ViewModelProviders.of(fragmentActivity).get(ViewModelMain::class.java)}
        activityViewModel?.cafeList?.observe(this, Observer {it?.let {
            adapter.refreshList(it)
        }}) //подписка на обновление листа
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}