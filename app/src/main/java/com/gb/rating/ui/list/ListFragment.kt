package com.gb.rating.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gb.rating.R
import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.models.CafeItem
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData() //лист с кафешками

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        //инициализируем адаптер и присваеваем его списку
        val adapter = ListAdapter()
        cafeListRecycler_FragmentList?.layoutManager = LinearLayoutManager(activity)
        cafeListRecycler_FragmentList?.adapter = adapter
        cafeList.observe(this, Observer {it?.let { adapter.refreshList(it) }}) //подписка на обновление листа
        val dbHelper = CafeDataSource(context)
        dbHelper.openR()
        cafeList.value = dbHelper.readAllCafe()
    }
}