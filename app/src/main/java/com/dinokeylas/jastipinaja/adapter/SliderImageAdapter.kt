package com.dinokeylas.jastipinaja.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dinokeylas.jastipinaja.R
import com.dinokeylas.jastipinaja.model.BannerImage

class SliderImageAdapter(context: Context, private val imageList: ArrayList<BannerImage>) :
    PagerAdapter() {

    private val ctx = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_slider_layout, view, false)!!
        val banner: ImageView = imageLayout.findViewById(R.id.slider_image)
        showImage(position, banner)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

    private fun showImage(position: Int, banner: ImageView) {
        val requestOption = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(ctx).load(imageList[position].url).apply(requestOption).into(banner)
    }
}