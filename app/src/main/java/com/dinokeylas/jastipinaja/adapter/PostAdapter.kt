package com.dinokeylas.jastipinaja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.R
import com.dinokeylas.jastipinaja.model.Post
import com.dinokeylas.jastipinaja.utils.DateUtils

class PostAdapter(
    private val context: Context,
    private val postList: ArrayList<Post>
) :
    RecyclerView.Adapter<PostAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_item_product, parent, false
            )
        )
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val date: String = if(postList[position].postType==1){
            DateUtils.getStringFormatedDate(postList[position].product.shoppingDate)
        } else{
            DateUtils.getStringFormatedDate(postList[position].product.ExpiredDate)
        }
        val price = "Rp " + postList[position].product.price.toString()

        holder.tvName.text = postList[position].product.name
        holder.tvDate.text = date
        holder.tvPrice.text = price
        holder.tvLocation.text = postList[position].product.locationCity
        Glide.with(context).load(postList[position].product.imageUrl).into(holder.ivProduct)
        holder.cardView.setOnClickListener(onClickListener(position))
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cv_item)
        val ivProduct: ImageView = itemView.findViewById(R.id.iv_product)
        val tvName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
    }

    /*this method must refactor to view task*/
    private fun onClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            // Toast.makeText(context, vegetableList[position].toString(), Toast.LENGTH_SHORT).show()
            moveToTransactionDetail(postList[position])
        }
    }

    private fun moveToTransactionDetail(post: Post){
        Toast.makeText(context, post.product.name, Toast.LENGTH_SHORT).show()
        TODO("Intent Here")
    }

}