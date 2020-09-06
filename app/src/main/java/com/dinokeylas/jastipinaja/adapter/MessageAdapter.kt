package com.dinokeylas.jastipinaja.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.R
import com.dinokeylas.jastipinaja.utils.DateUtils
import com.dinokeylas.jastipinaja.utils.MessageExample
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(
    private val context: Context,
    private val messageList: ArrayList<MessageExample>
) :
    RecyclerView.Adapter<MessageAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_item_message, parent, false
            )
        )
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if(messageList[position].imageUrl != "default profile image url"){
            Glide.with(context).load(messageList[position].imageUrl).into(holder.ivPerson)
        }
        holder.layoutMessage.setOnClickListener(onClickListener(position))
        holder.tvUserName.text = messageList[position].receiver.trim()
        holder.tvMessagePreview.text = messageList[position].preview.trim()
        holder.tvMessageDate.text = DateUtils.getStringFormatedDate(messageList[position].date)
        if (messageList[position].count>0){
          holder.tvUnreadMessageCount.visibility = View.VISIBLE
            holder.tvUnreadMessageCount.text = messageList[position].count.toString().trim()
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutMessage: ConstraintLayout = itemView.findViewById(R.id.layout_message)
        val ivPerson: CircleImageView = itemView.findViewById(R.id.civ_user_image)
        val tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
        val tvMessagePreview: TextView = itemView.findViewById(R.id.tv_message_preview)
        val tvMessageDate: TextView = itemView.findViewById(R.id.tv_message_date)
        val tvUnreadMessageCount: TextView = itemView.findViewById(R.id.tv_unread_message_count)
    }

    /*this method must refactor to view task*/
    private fun onClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            // Toast.makeText(context, vegetableList[position].toString(), Toast.LENGTH_SHORT).show()
            moveToTransactionDetail(messageList[position])
        }
    }

    private fun moveToTransactionDetail(message: MessageExample){
        TODO()
    }

}