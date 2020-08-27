package com.dinokeylas.jastipinaja

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dinokeylas.jastipinaja.adapter.CityAdapter
import com.dinokeylas.jastipinaja.adapter.SliderImageAdapter
import com.dinokeylas.jastipinaja.model.BannerImage
import com.dinokeylas.jastipinaja.model.City
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.BANNER_IMAGES
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.CITY
import com.google.firebase.firestore.FirebaseFirestore
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var viewPager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    private var imageList: ArrayList<BannerImage> = ArrayList()
    private var cityList: ArrayList<City> = ArrayList()
    private lateinit var ctx: View
    private lateinit var indicator: CirclePageIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        ctx = view
        viewPager = view.findViewById(R.id.pager)
        indicator = view.findViewById(R.id.indicator)

        showSliderImage()
        showCityList()

        return view
    }

    private fun showCityList(){
        FirebaseFirestore.getInstance().collection(CITY).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val city: City = document.toObject(City::class.java)
                    cityList.add(city)
                }
                fillCityToLayout()
            }.addOnFailureListener { Log.e("FETCH-CITY", it.message) }
    }

    private fun fillCityToLayout(){
        val recyclerView: RecyclerView = ctx.findViewById(R.id.rv_city)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val adapter = CityAdapter(context!!, cityList)
        recyclerView.adapter = adapter
    }

    private fun showSliderImage() {
        FirebaseFirestore.getInstance().collection(BANNER_IMAGES).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val image: BannerImage = document.toObject(BannerImage::class.java)
                    imageList.add(image)
                }
                fillBannerToLayout()
            }.addOnFailureListener { Log.e("FETCH-BANNER-IMAGE", it.message) }
    }

    private fun fillBannerToLayout(){
        viewPager?.adapter = SliderImageAdapter(ctx.context, imageList)
        setIndicator()
    }

    private fun setIndicator() {
        indicator.setViewPager(viewPager)
        val density = resources.displayMetrics.density
        indicator.radius = 5 * density
        NUM_PAGES = imageList.size

        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) { currentPage = 0 }
            viewPager!!.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) { currentPage = position }
            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) { }
            override fun onPageScrollStateChanged(pos: Int) { }
        })
    }
}
