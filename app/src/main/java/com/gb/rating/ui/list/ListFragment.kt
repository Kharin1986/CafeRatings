package com.gb.rating.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.models.CafeItem
import kotlinx.android.synthetic.main.fragment_list.*
import android.R



class ListFragment : Fragment() {

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

        var mViewModel : ListViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        mViewModel.cafeList.observe(this, Observer {it?.let { adapter.refreshList(it) }}) //подписка на обновление листа
        val dbHelper = CafeDataSource(context)
        dbHelper.openR()

        //mViewModel.cafeList.value = dbHelper.readAllCafe()
        mViewModel.retrieveCafeListByType("Россия", "Москва", "");
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}