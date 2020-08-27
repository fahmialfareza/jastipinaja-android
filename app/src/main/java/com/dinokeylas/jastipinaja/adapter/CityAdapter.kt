package com.dinokeylas.jastipinaja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.R
import com.dinokeylas.jastipinaja.model.City

class CityAdapter(private val context: Context, private val cities: ArrayList<City>) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_item_city, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityName.text = cities[position].name
        Glide.with(context).load(cities[position].imageUrl).into(holder.cityImage)
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.tv_city_name)
        val cityImage: ImageView = itemView.findViewById(R.id.iv_city)
    }
}