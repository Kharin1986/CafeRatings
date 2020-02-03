package com.gb.rating.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gb.rating.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val w = activity!!.windowManager
        val d: Display = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        val hScale = metrics.heightPixels.toFloat()/metrics.widthPixels.toFloat()

        cl_rest.layoutParams.height = (226*hScale*1.95).roundToInt()
        cl_rest.layoutParams.width = (158*3.0).roundToInt()
        cl_rest.requestLayout()

        cl_bar.layoutParams.height = (111*hScale*1.95).roundToInt()
        cl_bar.layoutParams.width = (158*3.0).roundToInt()
        cl_bar.requestLayout()

        cl_fav.layoutParams.height = (71*hScale*1.95).roundToInt()
        cl_fav.layoutParams.width = (158*3.0).roundToInt()
        cl_fav.requestLayout()

        cl_top.layoutParams.height = (71*hScale*1.95).roundToInt()
        cl_top.layoutParams.width = (158*3.0).roundToInt()
        cl_top.requestLayout()

        cl_cafe.layoutParams.height = (142*hScale*1.95).roundToInt()
        cl_cafe.layoutParams.width = (158*3.0).roundToInt()
        cl_cafe.requestLayout()

        cl_fastfood.layoutParams.height = (194*hScale*1.95).roundToInt()
        cl_fastfood.layoutParams.width = (158*3.0).roundToInt()
        cl_fastfood.requestLayout()
    }
}