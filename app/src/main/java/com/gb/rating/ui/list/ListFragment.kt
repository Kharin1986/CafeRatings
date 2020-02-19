package com.gb.rating.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.gb.rating.ui.ViewModelMain
import com.gb.rating.ui.cafeInfo.CafeInfoFragment



class ListFragment : Fragment() {
    lateinit var activityViewModel : ViewModelMain
    var mViewModel : ListViewModel? = null
    lateinit var adapter: ListAdapter


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
        adapter = ListAdapter{
            Log.d("BBB",it.name)

            val cafeInfoFragment = CafeInfoFragment()

            val ft = fragmentManager!!.beginTransaction()
            ft.replace(com.gb.rating.R.id.nav_host_fragment, cafeInfoFragment)
            ft.addToBackStack(null)
            ft.commit()


        }
        cafeListRecycler_FragmentList?.layoutManager = LinearLayoutManager(activity)
        cafeListRecycler_FragmentList?.adapter = adapter

        mViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        activity?.let { fragmentActivity ->  activityViewModel = ViewModelProvider(fragmentActivity).get(ViewModelMain::class.java)}
        activityViewModel.cafelist().observe(this, Observer {it?.let {
            adapter.refreshList(it)
        }}) //подписка на обновление листа
    }



    override fun onDestroyView() {
        super.onDestroyView()
    }
}